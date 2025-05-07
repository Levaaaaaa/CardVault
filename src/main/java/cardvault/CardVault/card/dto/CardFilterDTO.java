package cardvault.CardVault.search;

import cardvault.CardVault.enums.CardStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CardFilterDTO {
    String cardNumber;
    UUID cardOwnerId;
    LocalDateTime validityFrom;
    LocalDateTime validityTo;
    BigDecimal balanceMin;
    BigDecimal balanceMax;
    CardStatus status;
    LocalDateTime createAfter;
    LocalDateTime createBefore;
}
