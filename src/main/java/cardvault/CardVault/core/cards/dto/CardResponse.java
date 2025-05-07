package cardvault.CardVault.card.dto;

import cardvault.CardVault.dto.crud.UserDTO;
import cardvault.CardVault.enums.CardStatus;
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
    UUID cardUuid;
    String maskedCardNumber;
    UserDTO cardOwner;
    Date validityPeriod;
    BigDecimal amount;
    CardStatus status;
    LocalDateTime createdAt;
}
