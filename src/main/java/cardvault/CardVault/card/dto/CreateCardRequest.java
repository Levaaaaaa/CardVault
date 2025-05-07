package cardvault.CardVault.dto.crud;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCardRequest {
    @Pattern(regexp = "[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}", message = "ERROR_CODE_6")
    @NotNull(message = "ERROR_CODE_7")
    String cardNumber;

    @NotNull(message = "ERROR_CODE_9")
    @Future(message = "ERROR_CODE_8")
    Date validityPeriod;

    @NotNull(message = "ERROR_CODE_22")
    @DecimalMin(value = "0.01", message = "ERROR_CODE_22")
    BigDecimal startBalance;
}
