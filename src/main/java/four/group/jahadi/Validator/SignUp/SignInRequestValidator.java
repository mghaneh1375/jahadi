package four.group.jahadi.Validator.SignUp;

import four.group.jahadi.DTO.SignUp.SignInData;
import four.group.jahadi.Utility.Utility;
import org.json.JSONObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SignInRequestValidator implements ConstraintValidator<ValidatedSignInRequest, SignInData> {

    @Override
    public void initialize(ValidatedSignInRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SignInData value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if(
                value.getNid() == null || value.getPassword() == null ||
                        value.getPassword().isEmpty() || value.getNid().isEmpty()
        ) {
            errs.put("loginErr", "لطفا تمام اطلاعات لازم را وارد نمایید");
            isErrored = true;
        }

        if(!Utility.validationNationalCode(value.getNid()) ||
                value.getPassword().length() < 6
        ) {
            errs.put("loginErr", "نام کاربری و یا رمزعبور اشتباه است");
            isErrored = true;
        }

        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
