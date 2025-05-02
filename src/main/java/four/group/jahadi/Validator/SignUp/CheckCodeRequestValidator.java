package four.group.jahadi.Validator.SignUp;

import four.group.jahadi.DTO.SignUp.CheckCodeRequest;
import four.group.jahadi.Validator.PhoneValidator;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckCodeRequestValidator implements ConstraintValidator<ValidatedCheckCodeRequest, CheckCodeRequest> {

    @Autowired
    ModelMapper modelMapper;

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public void initialize(ValidatedCheckCodeRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public boolean isValid(CheckCodeRequest value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if(
                value.getCode() == null || value.getToken() == null ||
                        value.getPhone() == null
        ) {
            errs.put("data", "لطفا تمام اطلاعات لازم را وارد نمایید");
            isErrored = true;
        }

        if (!PhoneValidator.isValid(value.getPhone())) {
            errs.put("phone", "شماره همراه وارد شده معتبر نمی باشد");
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

        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;

    }

}
