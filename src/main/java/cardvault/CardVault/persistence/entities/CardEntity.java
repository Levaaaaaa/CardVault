package cardvault.CardVault.persistence.entities;

import cardvault.CardVault.enums.CardStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardEntity {
//    CREATE TABLE cards(
//            id SERIAL PRIMARY KEY,
//            card_number BYTEA NOT NULL,
//            card_owner bigint NOT NULL,
//            validity_period DATE NOT NULL,
//            status VARCHAR(10) NOT NULL,
//            created_at TIMESTAMP DEFAULT now()
//    CONSTRAINT fk_card_owner
//    FOREIGN KEY(card_owner)
//    REFERENCES users(id) ON DELETE CASCADE;
//);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Integer id;

    @Column(name = "card_number", nullable = false, columnDefinition = "BYTEA")
    byte[] cardNumber;

    @ManyToOne
    @JoinColumn(name = "card_owner", nullable = false, referencedColumnName = "id")
    UserEntity cardOwner;

    @Column(name = "validity_period", nullable = false)
    Date validityPeriod;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    CardStatus status;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;
}
