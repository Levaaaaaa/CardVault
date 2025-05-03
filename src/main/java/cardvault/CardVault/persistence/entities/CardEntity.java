package cardvault.CardVault.persistence.entities;

import cardvault.CardVault.enums.CardStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cards")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CardEntity {
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    UUID id;

    @Column(name = "card_number", nullable = false, columnDefinition = "BYTEA", unique = true)
    byte[] cardNumber;

    @Column(name = "card_number_hash", nullable = false, unique = true, columnDefinition = "BYTEA")
    byte[] cardNumberHash;
    @ManyToOne
    @JoinColumn(name = "card_owner", nullable = false, referencedColumnName = "id")
    UserEntity cardOwner;

    @Column(name = "validity_period", nullable = false)
    Date validityPeriod;

    @Column(name = "balance", precision = 19, scale = 2, nullable = false)
    BigDecimal balance;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    CardStatus status;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;
}
