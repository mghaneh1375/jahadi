package four.group.jahadi.Validator.SignUp;

import four.group.jahadi.DTO.SignUp.PersonSignUpCheckPhoneData;
import four.group.jahadi.Validator.PhoneValidator;
import org.json.JSONObject;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PersonSignUpFormCheckPhoneValidator implements ConstraintValidator<ValidatedPersonSignUpFormCheckPhone, PersonSignUpCheckPhoneData> {

    @Override
    public void initialize(ValidatedPersonSignUpFormCheckPhone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(PersonSignUpCheckPhoneData value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if(value.getPhone() == null) {
            errs.put("data", "لطفا شماره همراه را وارد نمایید");
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
            return false;
        }

        if (ObjectUtils.isEmpty(value.getPhone()) || !PhoneValidator.isValid(value.getPhone())) {
            errs.put("phone", "شماره همراه وارد شده معتبر نمی باشد");
            isErrored = true;
        }

        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
