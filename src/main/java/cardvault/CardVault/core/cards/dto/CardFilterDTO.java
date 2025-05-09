package cardvault.CardVault.core.cards.dto;

import cardvault.CardVault.core.cards.CardStatus;
import cardvault.CardVault.error_handler.validators.enum_validator.ValidEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}", message = "ERROR_CODE_6")
    String cardNumber;

    @Pattern(
            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "ERROR_CODE_27")
    UUID cardOwnerId;

    LocalDateTime validityFrom;
    LocalDateTime validityTo;

    @DecimalMin(value = "0.01", message = "ERROR_CODE_22")
    BigDecimal balanceMin;

    @DecimalMin(value = "0.01", message = "ERROR_CODE_22")
    BigDecimal balanceMax;

    @ValidEnum(enumClass = CardStatus.class, message = "ERROR_CODE_28")
    CardStatus status;

    LocalDateTime createAfter;
    LocalDateTime createBefore;
}
