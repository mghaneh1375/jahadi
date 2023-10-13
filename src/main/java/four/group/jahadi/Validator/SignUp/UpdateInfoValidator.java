package four.group.jahadi.Validator.SignUp;

import four.group.jahadi.DTO.SignUp.*;
import four.group.jahadi.Validator.DateValidator;
import four.group.jahadi.Validator.PhoneValidator;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UpdateInfoValidator implements ConstraintValidator<ValidatedUpdateInfo, UpdateInfoData> {

    @Autowired
    ModelMapper modelMapper;

    @Override
    public void initialize(ValidatedUpdateInfo constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UpdateInfoData value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if (value.getName() != null && value.getName().length() < 3) {
            errs.put("name", "لطفا نام خود را وارد نمایید");
            isErrored = true;
        }

        if (value.getFatherName() != null && value.getFatherName().length() < 3) {
            errs.put("fatherName", "لطفا نام پدر خود را وارد نمایید");
            isErrored = true;
        }

        if (value.getUniversity() != null && value.getUniversity().length() < 3) {
            errs.put("university", "لطفا دانشگاه خود را وارد نمایید");
            isErrored = true;
        }

        if (value.getField() != null && value.getField().length() < 3) {
            errs.put("field", "لطفا رشته تحصیلی خود را وارد نمایید");
            isErrored = true;
        }

        if (value.getUniversityYear() != null) {
            errs.put("universityYear", "سال تحصیل نامعتبر است");
            isErrored = true;
        }

        if (value.getBirthDay() != null && (
                ObjectUtils.isEmpty(value.getBirthDay()) ||
                        !DateValidator.isValid(value.getBirthDay()) ||
                        Integer.parseInt(value.getBirthDay().split("\\/")[0]) > 1385)
        ) {
            errs.put("birthDay", "تاریخ تولد وارد شده معتبر نمی باشد");
            isErrored = true;
        }

        if (value.getNearbyName() != null && value.getNearbyName().length() < 3) {
            errs.put("nearbyName", "لطفا نام و نسبت فرد مذکور را وارد نمایید");
            isErrored = true;
        }

        if (value.getNearbyPhone() != null && (
                ObjectUtils.isEmpty(value.getNearbyPhone()) || !PhoneValidator.isValid(value.getNearbyPhone())
        )) {
            errs.put("nearbyPhone", "شماره همراه وارد شده معتبر نمی باشد");
            isErrored = true;
        }

        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
