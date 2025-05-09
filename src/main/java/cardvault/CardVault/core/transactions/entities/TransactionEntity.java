package cardvault.CardVault.core.transactions.entities;

import cardvault.CardVault.core.cards.entity.CardEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TransactionEntity {
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
