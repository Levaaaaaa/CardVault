package cardvault.CardVault.dto.transfer;

import cardvault.CardVault.card.dto.CardResponse;
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
