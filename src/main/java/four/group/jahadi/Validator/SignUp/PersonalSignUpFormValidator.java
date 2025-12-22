package four.group.jahadi.Validator.SignUp;

import four.group.jahadi.DTO.SignUp.PersonalSignUpData;
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

public class PersonalSignUpFormValidator implements ConstraintValidator<ValidatedPersonalSignUpForm, PersonalSignUpData> {

    @Autowired
    ModelMapper modelMapper;

    @Override
    public void initialize(ValidatedPersonalSignUpForm constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(PersonalSignUpData value, ConstraintValidatorContext context) {

        if (ObjectUtils.isEmpty(value.getPhone()) || !PhoneValidator.isValid(value.getPhone())) {
            JSONObject errs = new JSONObject();
            errs.put("phone", "شماره همراه وارد شده معتبر نمی باشد");
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
            return false;
        }

        if(!new SignUpFormStep1Validator().isValid(modelMapper.map(value, SignUpStep1Data.class), context))
            return false;

        SignUpStep2Data signUpStep2Data = modelMapper.map(value, SignUpStep2Data.class);
        if(!new SignUpFormStep2Validator().isValid(signUpStep2Data, context))
            return false;

        SignUpStep3Data signUpStep3Data = modelMapper.map(value, SignUpStep3Data.class);
        return new SignUpFormStep3Validator().isValid(signUpStep3Data, context);
    }

}
