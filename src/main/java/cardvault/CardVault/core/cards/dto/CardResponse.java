package cardvault.CardVault.core.cards.dto;

import cardvault.CardVault.core.users.dto.UserDTO;
import cardvault.CardVault.core.cards.CardStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardResponse {
    UUID id;
    String maskedCardNumber;
    UserDTO cardOwner;
    Date validityPeriod;
    BigDecimal amount;
    CardStatus status;
    LocalDateTime createdAt;
}
