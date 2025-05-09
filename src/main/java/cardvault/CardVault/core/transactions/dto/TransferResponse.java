package cardvault.CardVault.core.transactions.dto;

import cardvault.CardVault.core.cards.dto.CardResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TransferResponse {
    CardResponse producer;
    CardResponse consumer;
    BigDecimal amount;
    LocalDateTime transferDate;
    String message;
}
