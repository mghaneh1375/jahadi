package four.group.jahadi.Validator.SignUp;

import four.group.jahadi.DTO.SignUp.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GroupSignUpFormValidator implements ConstraintValidator<ValidatedGroupSignUpForm, GroupSignUpFormValidator> {

    @Autowired
    ModelMapper modelMapper;

    @Override
    public void initialize(ValidatedGroupSignUpForm constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(GroupSignUpFormValidator value, ConstraintValidatorContext context) {

        SignUpStep1Data signUpStep1Data = modelMapper.map(value, SignUpStep1Data.class);

        if(!new SignUpFormStep1Validator().isValid(signUpStep1Data, context))
            return false;

        SignUpStep2ForGroupData signUpStep2ForGroupData = modelMapper.map(value, SignUpStep2ForGroupData.class);

        return new SignUpFormStep2ForGroupsValidator().isValid(signUpStep2ForGroupData, context);
    }

}
