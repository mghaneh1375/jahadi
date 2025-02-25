package four.group.jahadi.Validator;

import four.group.jahadi.DTO.Area.UpdateAreaData;
import org.json.JSONObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UpdateAreaValidator implements ConstraintValidator<ValidatedUpdateArea, UpdateAreaData> {

    @Override
    public void initialize(ValidatedUpdateArea constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UpdateAreaData value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if(value.getAreaId() == null || value.getColor() == null || value.getOwner() == null) {
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
