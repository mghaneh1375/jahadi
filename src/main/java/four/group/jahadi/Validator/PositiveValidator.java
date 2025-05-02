package four.group.jahadi.Validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PositiveValidator implements ConstraintValidator<Positive, Integer> {


    @Override
@four.group.jahadi.Utility.KeepMethodName
    public void initialize(Positive constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if(integer == null) return true;
        return integer >= 0;
    }
}
