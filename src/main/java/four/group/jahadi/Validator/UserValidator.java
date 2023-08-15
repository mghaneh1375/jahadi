package four.group.jahadi.Validator;

import four.group.jahadi.DTO.UserData;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Utility.Utility;
import org.json.JSONObject;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserValidator implements ConstraintValidator<ValidatedUser, UserData> {

    @Override
    public void initialize(ValidatedUser constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserData value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if(
                value.getFirstName() == null || value.getLastName() == null ||
                        value.getPhone() == null || value.getNid() == null ||
                        value.getPassword() == null
        ) {
            errs.put("data", "لطفا تمام اطلاعات را وارد نمایید");
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
            return false;
        }

        if(value.getFirstName().length() < 3 || value.getLastName().length() < 3) {
            errs.put("name", "لطفا نام خود را وارد نمایید");
            isErrored = true;
        }

        if(value.getPassword().length() < 6) {
            errs.put("password", "رمزعبور وارد شده معتبر نمی باشد");
            isErrored = true;
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

        if(ObjectUtils.isEmpty(value.getSex()) ||
                !EnumValidatorImp.isValid(value.getSex(), Sex.class)
        ) {
            errs.put("sex", "لطفا جنسیت خود را وارد نمایید");
            isErrored = true;
        }

        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
