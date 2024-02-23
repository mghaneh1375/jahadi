package four.group.jahadi.Validator;

import four.group.jahadi.DTO.Area.AreaData;
import org.json.JSONObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AreaValidator implements ConstraintValidator<ValidatedArea, AreaData> {

    @Override
    public void initialize(ValidatedArea constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(AreaData value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if(value.getColor() == null || value.getOwner() == null) {
            errs.put("data", "لطفا تمام اطلاعات را وارد نمایید");
            isErrored = true;
        }

        if(value.getName() == null || value.getName().length() < 2) {
            errs.put("title", "لطفا عنوان را وارد نمایید");
            isErrored = true;
        }

        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
