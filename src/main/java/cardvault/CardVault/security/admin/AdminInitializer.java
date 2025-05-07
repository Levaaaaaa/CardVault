package cardvault.CardVault.security.admin;

import cardvault.CardVault.enums.UserRole;
import cardvault.CardVault.persistence.entities.UserEntity;
import cardvault.CardVault.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class AdminInitializer implements CommandLineRunner {
    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            UserEntity admin = UserEntity.builder().id(UUID.randomUUID())
                    .firstName("admin")
                    .lastName("admin")
                    .password(passwordEncoder.encode(adminPassword))
                    .email(adminEmail)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .userRole(UserRole.ROLE_ADMIN)
                    .enabled(true)
                    .build();
            userRepository.save(admin);
        }
    }
}
