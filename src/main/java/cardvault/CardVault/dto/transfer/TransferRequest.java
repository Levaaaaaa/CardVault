package cardvault.CardVault.dto.transfer;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TransferRequest {
    @NotNull(message = "ERROR_CODE_7")
    @Pattern(
            regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$",
            message = "ERROR_CODE_19")
    String producer;

    @NotNull(message = "ERROR_CODE_7")
    @Pattern(
            regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$",
            message = "ERROR_CODE_19")
    String consumer;

    @NotNull(message = "ERROR_CODE_22")
    @DecimalMin(value = "0.01", message = "ERROR_CODE_22")
    BigDecimal amount;
}
