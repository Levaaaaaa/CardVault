package cardvault.CardVault.initializer;

import cardvault.CardVault.core.cards.CardStatus;
import cardvault.CardVault.core.cards.entity.CardEntity;
import cardvault.CardVault.core.cards.repositories.CardRepository;
import cardvault.CardVault.core.users.UserRole;
import cardvault.CardVault.core.users.entities.UserEntity;
import cardvault.CardVault.core.users.repositories.UserRepository;
import cardvault.CardVault.security.encryption.EncryptionService;
import cardvault.CardVault.security.hash.HashService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static cardvault.CardVault.core.users.UserRole.ROLE_USER;

@Component
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private HashService hashService;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() > 0) {
            return;
        }
        UserEntity alice = UserEntity.builder()
                    .id(UUID.randomUUID())
                    .email("alice@example.com")
                    .password(new String(encryptionService.encrypt("password1")))
                    .userRole(ROLE_USER)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .enabled(true)
                    .firstName("alice")
                    .lastName("Alice")
                    .build();

        UserEntity bob = UserEntity.builder()
                    .id(UUID.randomUUID())
                    .email("bob@example.com")
                    .password(new String(encryptionService.encrypt("password2")))
                    .userRole(ROLE_USER)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .enabled(true)
                    .firstName("bob")
                    .lastName("Bob")
                    .build();
        UserEntity admin = UserEntity.builder().id(UUID.randomUUID())
                .firstName("admin")
                .lastName("admin")
                .password(new String(encryptionService.encrypt(adminPassword)))
                .email(adminEmail)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .userRole(UserRole.ROLE_ADMIN)
                .enabled(true)
                .build();
        userRepository.saveAll(List.of(alice, bob, admin));

        for (int i = 0; i < 10; i++) {
            String plainCardNumber = "40000000000000" + i;

            byte[] encryptedCardNumber = encryptionService.encrypt(plainCardNumber);
            byte[] cardNumberHash = hashService.hash(plainCardNumber);

            CardEntity card = CardEntity.builder()
                    .id(UUID.randomUUID())
                    .cardNumber(encryptedCardNumber)
                    .cardNumberHash(cardNumberHash)
                    .cardOwner(i % 2 == 0 ? alice : bob)
                    .validityPeriod(Date.valueOf(
                            LocalDate.now().plusYears(2)
                    ))
                    .balance(BigDecimal.valueOf(1000 + i * 100))
                    .status(CardStatus.ACTIVE)
                    .createdAt(LocalDateTime.now())
                    .build();

            cardRepository.save(card);
        }

    }
}
