package four.group.jahadi.Validator;


import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    @Autowired
    private PasswordValidator passwordValidator;

    @Override
    public void initialize(StrongPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        PasswordValidator.ValidationResult result = passwordValidator.validate(password);

        if (!result.isValid()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.join(", ", result.getMessages())
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
