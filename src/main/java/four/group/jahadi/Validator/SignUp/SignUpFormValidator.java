package four.group.jahadi.Validator.SignUp;

import four.group.jahadi.DTO.SignUp.SignUpData;
import four.group.jahadi.DTO.SignUp.SignUpStep1Data;
import four.group.jahadi.DTO.SignUp.SignUpStep2Data;
import four.group.jahadi.DTO.SignUp.SignUpStep3Data;
import four.group.jahadi.Validator.PhoneValidator;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SignUpFormValidator implements ConstraintValidator<ValidatedSignUpForm, SignUpData> {

    @Autowired
    ModelMapper modelMapper;

    @Override
    public void initialize(ValidatedSignUpForm constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SignUpData value, ConstraintValidatorContext context) {

        if (ObjectUtils.isEmpty(value.getPhone()) || !PhoneValidator.isValid(value.getPhone())) {
            JSONObject errs = new JSONObject();
            errs.put("phone", "شماره همراه وارد شده معتبر نمی باشد");
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
            return false;
        }

        SignUpStep2Data signUpStep2Data = modelMapper.map(value, SignUpStep2Data.class);

        if(!new SignUpFormStep2Validator().isValid(signUpStep2Data, context))
            return false;

        SignUpStep3Data signUpStep3Data = modelMapper.map(value, SignUpStep3Data.class);

        return new SignUpFormStep3Validator().isValid(signUpStep3Data, context);
    }

}
