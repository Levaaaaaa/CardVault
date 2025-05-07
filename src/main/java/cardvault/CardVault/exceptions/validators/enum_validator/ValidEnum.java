package cardvault.CardVault.exceptions.valid_annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {
    Class<? extends Enum<?>> enumClass();
    String message() default "Not supported value. Fail enum validation";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
