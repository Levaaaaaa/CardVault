package cardvault.CardVault.exceptions.valid_annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ValidPassword {
    String message() default "Invalid password!";
    int min() default 0;
    int max() default 30;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
