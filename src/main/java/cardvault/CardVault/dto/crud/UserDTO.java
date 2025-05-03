package cardvault.CardVault.dto.crud;

import cardvault.CardVault.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserDTO {
    String firstName;
    String lastName;
    String email;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    boolean enabled;
}
