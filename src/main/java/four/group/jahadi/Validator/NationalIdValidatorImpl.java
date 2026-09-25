package four.group.jahadi.Validator;


import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NationalIdValidatorImpl implements ConstraintValidator<ValidNationalId, String> {

    @Autowired
    private NationalIdValidator nationalIdValidator;

    @Override
    public void initialize(ValidNationalId constraintAnnotation) {
    }

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        if (code == null)
            return false;

        return nationalIdValidator.validate(code);
    }
}
