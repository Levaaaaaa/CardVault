package cardvault.CardVault.dto.login;

import cardvault.CardVault.exceptions.valid_annotations.ValidPassword;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {

    @NotNull(message = "ERROR_CODE_14")
    @Pattern(regexp = "[A-Za-z0-9]+@[a-z]+.[a-z]+", message = "ERROR_CODE_15")
    @Size(max = 100, message = "ERROR_CODE_16")
    private String email;

    @NotNull(message = "ERROR_CODE_18")
    @ValidPassword(message = "ERROR_CODE_17", min = 4, max = 15)
    private char[] password;
}
