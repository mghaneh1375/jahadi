package four.group.jahadi.Validator;

import four.group.jahadi.Utility.Utility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class TimeValidator implements
        ConstraintValidator<TimeConstraint, String> {

    private static final String regex = "(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]";
    private static final Pattern pattern = Pattern.compile(regex);

    public static boolean isValid(String s) {

        if (s.length() == 4)
            s = "0" + s;

        return pattern.matcher(Utility.convertPersianDigits(s)).matches();
    }

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public void initialize(TimeConstraint constraintAnnotation) {
    }

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if (s.length() == 4)
            s = "0" + s;

        return pattern.matcher(Utility.convertPersianDigits(s)).matches();
    }

}
