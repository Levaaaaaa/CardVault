package cardvault.CardVault.core.users.services;

import cardvault.CardVault.core.users.UserRole;
import cardvault.CardVault.core.users.entities.UserEntity;
import cardvault.CardVault.core.users.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetCurrentUserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity getCurrentUser() {
        Optional<UserEntity> optional = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("ERROR_CODE_4");
        }
        return optional.get();
    }

    public boolean isAdmin() {
        Optional<UserEntity> optional = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("ERROR_CODE_4");
        }
        return UserRole.ROLE_ADMIN.equals(optional.get().getUserRole());
    }
}