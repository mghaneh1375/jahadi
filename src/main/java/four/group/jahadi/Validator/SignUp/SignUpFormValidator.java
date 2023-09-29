package four.group.jahadi.Validator.SignUp;

import four.group.jahadi.DTO.SignUp.SignUpData;
import four.group.jahadi.DTO.SignUp.SignUpStep1Data;
import four.group.jahadi.DTO.SignUp.SignUpStep2Data;
import four.group.jahadi.DTO.SignUp.SignUpStep3Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

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

        SignUpStep1Data signUpStep1Data = modelMapper.map(value, SignUpStep1Data.class);

        if(!new SignUpFormStep1Validator().isValid(signUpStep1Data, context))
            return false;

        SignUpStep2Data signUpStep2Data = modelMapper.map(value, SignUpStep2Data.class);

        if(!new SignUpFormStep2Validator().isValid(signUpStep2Data, context))
            return false;

        SignUpStep3Data signUpStep3Data = modelMapper.map(value, SignUpStep3Data.class);

        return new SignUpFormStep3Validator().isValid(signUpStep3Data, context);
    }

}
