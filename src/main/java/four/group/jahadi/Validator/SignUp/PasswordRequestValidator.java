package four.group.jahadi.Validator.SignUp;

import four.group.jahadi.DTO.SignUp.PasswordData;
import org.json.JSONObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordRequestValidator implements ConstraintValidator<ValidatedPasswordRequest, PasswordData> {

    @Override
    public void initialize(ValidatedPasswordRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(PasswordData value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if(
                value.getPassword() == null || value.getRepeatPassword() == null
        ) {
            errs.put("loginErr", "لطفا تمام اطلاعات لازم را وارد نمایید");
            isErrored = true;
        }

        else {

            if(!value.getRepeatPassword().equals(value.getPassword())) {
                errs.put("password", "رمزعبور وارد شده با تکرار آن یکسان نیست");
                isErrored = true;
            }

            if(value.getPassword().length() < 6) {
                errs.put("password", "رمزعبور وارد شده معتبر نمی باشد");
                isErrored = true;
            }

        }

        if (isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
