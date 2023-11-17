package four.group.jahadi.Validator.SignUp;

import four.group.jahadi.DTO.SignUp.CheckForgetPassCodeRequest;
import four.group.jahadi.Utility.Utility;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckForgetPassCodeRequestValidator implements
        ConstraintValidator<ValidatedCheckForgetPassCodeRequest, CheckForgetPassCodeRequest> {

    @Autowired
    ModelMapper modelMapper;

    @Override
    public void initialize(ValidatedCheckForgetPassCodeRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CheckForgetPassCodeRequest value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if (
                value.getCode() == null || value.getToken() == null ||
                        value.getNid() == null
        ) {
            errs.put("data", "لطفا تمام اطلاعات لازم را وارد نمایید");
            isErrored = true;
        }

        if (ObjectUtils.isEmpty(value.getNid()) || !Utility.validationNationalCode(value.getNid())) {
            errs.put("NID", "کد ملی وارد شده معتبر نمی باشد");
            isErrored = true;
        }

        if (value.getToken().length() != 20) {
            errs.put("token", "توکن وارد شده نامعتبر است");
            isErrored = true;
        }

        if (value.getCode() < 100000 || value.getCode() > 999999) {
            errs.put("code", "کد وارد شده نامعتبر است");
            isErrored = true;
        }

        if (isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;

    }

}
