package four.group.jahadi.Validator;

import four.group.jahadi.DTO.ProjectData;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PositiveValidator implements ConstraintValidator<Positive, Integer> {


    @Override
    public void initialize(Positive constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if(integer == null) return true;
        return integer >= 0;
    }
}
