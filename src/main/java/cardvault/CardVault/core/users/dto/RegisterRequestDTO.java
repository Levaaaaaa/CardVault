package cardvault.CardVault.core.users.dto;

import cardvault.CardVault.error_handler.validators.password_validator.ValidPassword;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    @NotBlank(message = "ERROR_CODE_10")
    @Size(max = 100, message = "ERROR_CODE_11")
    private String firstName;

    @NotBlank(message = "ERROR_CODE_12")
    @Size(max = 100, message = "ERROR_CODE_13")
    private String lastName;

    @NotNull(message = "ERROR_CODE_14")
    @Pattern(regexp = "[A-Za-z0-9]+@[a-z]+.[a-z]+", message = "ERROR_CODE_15")
    @Size(max = 100, message = "ERROR_CODE_16")
    private String email;

    @NotNull(message = "ERROR_CODE_18")
    @ValidPassword(message = "ERROR_CODE_17", min = 4, max = 15)
    private char[] password;
}
