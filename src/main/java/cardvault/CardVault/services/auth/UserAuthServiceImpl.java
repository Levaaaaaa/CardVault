package cardvault.CardVault.services.auth;

import cardvault.CardVault.dto.login.LoginRequestDTO;
import cardvault.CardVault.dto.register.RegisterRequestDTO;
import cardvault.CardVault.enums.UserRole;
import cardvault.CardVault.persistence.entities.UserEntity;
import cardvault.CardVault.persistence.repositories.UserRepository;
import cardvault.CardVault.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;
import java.nio.CharBuffer;
import java.util.Optional;

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
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encodedPassword.getBytes())
                .userRole(UserRole.USER)
                .build();

        userRepository.save(userEntity);
        return jwtService.generateToken(userEntity.getEmail());
    }

    @Override
    public String authenticateUser(LoginRequestDTO request) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User with email " + request.getEmail() + " not found!");
        }
        UserEntity foundUser = optionalUser.get();
        if (!passwordEncoder.matches(CharBuffer.wrap(request.getPassword()), new String(foundUser.getPassword()))) {
            throw new RuntimeException("Invalid credentials!");
        }

        return jwtService.generateToken(foundUser.getEmail());
    }
}
