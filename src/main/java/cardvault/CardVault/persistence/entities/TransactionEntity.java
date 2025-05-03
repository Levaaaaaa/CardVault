package cardvault.CardVault.persistence.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionEntity {
//    CREATE TABLE transactions(
//            id SERIAL PRIMARY KEY,
//            producer BIGINT NOT NULL,
//            consumer BIGINT NOT NULL,
//            transaction_date timestamp not null,
//            amount DECIMAL(12, 2) NOT NULL CHECK(amount > 0),
//    CONSTRAINT fk_consumer
//    FOREIGN KEY (consumer)
//    REFERENCES cards(id),
//    CONSTRAINT fk_producer
//    FOREIGN KEY (producer)
//    REFERENCES cards(id),
//);
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    UUID id;

    @ManyToOne
    @JoinColumn(name = "producer", referencedColumnName = "id", nullable = false)
    CardEntity producer;

    @ManyToOne
    @JoinColumn(name = "consumer", referencedColumnName = "id", nullable = false)
    CardEntity consumer;

    @Column(name = "transaction_date", nullable = false)
    LocalDateTime transactionDate;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    @Check(constraints = "amount > 0")
    BigDecimal amount;
}
