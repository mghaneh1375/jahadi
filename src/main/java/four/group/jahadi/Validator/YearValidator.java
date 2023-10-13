package four.group.jahadi.Validator;

import four.group.jahadi.Utility.Utility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class YearValidator implements ConstraintValidator<Year, String> {

    private static final Pattern pattern = Pattern.compile("^(13|14)(\\d{2})$");


    @Override
    public void initialize(Year constraintAnnotation) {
    }

    @Override
    public boolean isValid(String in, ConstraintValidatorContext constraintValidatorContext) {
        return pattern.matcher(Utility.convertPersianDigits(in)).matches();
    }
}
