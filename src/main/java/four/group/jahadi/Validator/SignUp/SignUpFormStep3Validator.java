package four.group.jahadi.Validator.SignUp;

import four.group.jahadi.DTO.SignUp.SignUpStep3Data;
import four.group.jahadi.Validator.PhoneValidator;
import org.json.JSONObject;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SignUpFormStep3Validator implements ConstraintValidator<ValidatedSignUpFormStep3, SignUpStep3Data> {

    @Override
    public void initialize(ValidatedSignUpFormStep3 constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SignUpStep3Data value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if(value.getNearbyName() == null || value.getNearbyPhone() == null) {
            errs.put("data", "لطفا اطلاعات تمامی فیلدها را وارد نمایید");

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();

            return false;
        }

        if(value.getNearbyName().length() < 3) {
            errs.put("nearbyName", "لطفا نام و نسبت فرد مذکور را وارد نمایید");
            isErrored = true;
        }

        if(value.getGroupCode() != null && value.getGroupCode().toString().length() != 6) {
            errs.put("groupCode", "کد گروه نامعتبر است");
            isErrored = true;
        }

        if (ObjectUtils.isEmpty(value.getNearbyPhone()) || !PhoneValidator.isValid(value.getNearbyPhone())) {
            errs.put("nearbyPhone", "شماره همراه وارد شده معتبر نمی باشد");
            isErrored = true;
        }

        if(
                value.getPasswordRepeat() == null ||
                        value.getPassword() == null
        ) {
            errs.put("data", "لطفا رمزعبور و تکرار آن را وارد نمایید");
            isErrored = true;
        }

        if(!value.getPasswordRepeat().equals(value.getPassword())) {
            errs.put("password", "رمزعبور وارد شده با تکرار آن یکسان نیست");
            isErrored = true;
        }

        if(value.getPassword().length() < 6) {
            errs.put("password", "رمزعبور وارد شده معتبر نمی باشد");
            isErrored = true;
        }

        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
