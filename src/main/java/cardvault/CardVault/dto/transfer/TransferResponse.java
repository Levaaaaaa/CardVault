package cardvault.CardVault.dto.transfer;

import cardvault.CardVault.dto.crud.CardResponse;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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
public class TransferResponse {
    CardResponse producer;
    CardResponse consumer;
    BigDecimal amount;
    LocalDateTime transferDate;
    String message;
}
