package four.group.jahadi.Validator.ModuleForms;

import four.group.jahadi.DTO.ModuleForms.ExperimentalFormDTO;
import org.json.JSONObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExperimentalFormValidator implements ConstraintValidator<ValidatedExperimentalForm, ExperimentalFormDTO> {

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public void initialize(ValidatedExperimentalForm constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }


    @Override
@four.group.jahadi.Utility.KeepMethodName
    public boolean isValid(ExperimentalFormDTO value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if(
                value.getExperiment() == null
        ) {
            errs.put("data", "لطفا نوع آزمایش را وارد نمایید");
            isErrored = true;
        }

        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
