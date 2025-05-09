package cardvault.CardVault.core.users.dto;

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
