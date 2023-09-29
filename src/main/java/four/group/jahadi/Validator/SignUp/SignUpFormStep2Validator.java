package four.group.jahadi.Validator.SignUp;

import four.group.jahadi.DTO.SignUp.SignUpStep2Data;
import org.json.JSONObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SignUpFormStep2Validator implements ConstraintValidator<ValidatedSignUpFormStep2, SignUpStep2Data> {

    @Override
    public void initialize(ValidatedSignUpFormStep2 constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SignUpStep2Data value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if(value.getBloodType() == null) {
            errs.put("bloodType", "لطفا گروه خونی خود را وارد نمایید");
            isErrored = true;
        }


        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
