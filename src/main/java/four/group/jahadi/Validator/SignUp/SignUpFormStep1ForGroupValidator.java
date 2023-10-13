package four.group.jahadi.Validator.SignUp;

import four.group.jahadi.DTO.SignUp.SignUpStep1ForGroupData;
import four.group.jahadi.Utility.Utility;
import four.group.jahadi.Validator.DateValidator;
import four.group.jahadi.Validator.PhoneValidator;
import org.json.JSONObject;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SignUpFormStep1ForGroupValidator implements ConstraintValidator<ValidatedSignUpFormStep1ForGroup, SignUpStep1ForGroupData> {

    @Override
    public void initialize(ValidatedSignUpFormStep1ForGroup constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SignUpStep1ForGroupData value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if(
                value.getName() == null || value.getFatherName() == null ||
                        value.getPhone() == null || value.getNid() == null ||
                        value.getBirthDay() == null || value.getUniversityYear() == null ||
                        value.getSex() == null || value.getField() == null || value.getUniversity() == null ||
                        value.getCid() == null || value.getEndManageYear() == null || value.getPassword() == null ||
                        value.getPasswordRepeat() == null
        ) {
            errs.put("data", "لطفا تمام اطلاعات را وارد نمایید");
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
            return false;
        }

        if(ObjectUtils.isEmpty(value.getBirthDay()) ||
                !DateValidator.isValid(value.getBirthDay()) ||
                Integer.parseInt(value.getBirthDay().split("\\/")[0]) > 1385
        ) {
            errs.put("birthDay", "تاریخ تولد وارد شده معتبر نمی باشد");
            isErrored = true;
        }

        if (ObjectUtils.isEmpty(value.getNid()) || !Utility.validationNationalCode(value.getNid())) {
            errs.put("NID", "کد ملی وارد شده معتبر نمی باشد");
            isErrored = true;
        }

        if (ObjectUtils.isEmpty(value.getPhone()) || !PhoneValidator.isValid(value.getPhone())) {
            errs.put("phone", "شماره همراه وارد شده معتبر نمی باشد");
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
