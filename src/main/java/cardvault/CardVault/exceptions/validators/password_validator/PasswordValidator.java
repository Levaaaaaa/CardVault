package cardvault.CardVault.exceptions.valid_annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, char[]> {

    private int min;
    private int max;
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(char[] bytes, ConstraintValidatorContext constraintValidatorContext) {
        if (bytes == null) {
            return true;
        }
        return bytes.length >= min && bytes.length <= max;
    }
}
