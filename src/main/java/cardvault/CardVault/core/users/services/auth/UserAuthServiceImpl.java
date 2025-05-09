package cardvault.CardVault.core.users.services.auth;

import cardvault.CardVault.core.users.dto.LoginRequestDTO;
import cardvault.CardVault.core.users.dto.RegisterRequestDTO;
import cardvault.CardVault.core.users.UserRole;
import cardvault.CardVault.core.users.entities.UserEntity;
import cardvault.CardVault.core.users.repositories.UserRepository;
import cardvault.CardVault.security.jwt.JwtService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
class UserAuthServiceImpl implements UserAuthService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public String registerUser(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with email " + " already exists!");
        }

        String encodedPassword = passwordEncoder.encode(CharBuffer.wrap(request.getPassword()));
        UserEntity userEntity = UserEntity.builder()
                .id(UUID.randomUUID())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encodedPassword)
                .userRole(UserRole.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .enabled(true)
                .build();

        userRepository.save(userEntity);
        return jwtService.generateToken(userEntity.getEmail());
    }

    @Override
    public String authenticateUser(LoginRequestDTO request) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("ERROR_CODE_21");
        }
        UserEntity foundUser = optionalUser.get();
        if (!passwordEncoder.matches(CharBuffer.wrap(request.getPassword()), foundUser.getPassword())) {
            throw new BadCredentialsException("ERROR_CODE_20");
        }

        return jwtService.generateToken(foundUser.getEmail());
    }
}
