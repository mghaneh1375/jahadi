package four.group.jahadi.Validator;

import four.group.jahadi.Utility.Utility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class JustNumberValidator implements ConstraintValidator<JustNumeric, String> {

    private static final Pattern pattern = Pattern.compile("^[0-9]*$");


    @Override
@four.group.jahadi.Utility.KeepMethodName
    public void initialize(JustNumeric constraintAnnotation) {
    }

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public boolean isValid(String in, ConstraintValidatorContext constraintValidatorContext) {
        if(in == null) return true;
        return pattern.matcher(Utility.convertPersianDigits(in)).matches();
    }
}
