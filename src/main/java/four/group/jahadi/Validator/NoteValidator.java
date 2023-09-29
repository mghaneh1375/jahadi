package four.group.jahadi.Validator;

import four.group.jahadi.DTO.NoteData;
import org.json.JSONObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NoteValidator implements ConstraintValidator<ValidatedNote, NoteData> {

    @Override
    public void initialize(ValidatedNote constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(NoteData value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if(value.getTitle() == null || value.getTitle().length() < 2) {
            errs.put("title", "لطفا عنوان را وارد نمایید");
            isErrored = true;
        }

        if(value.getDescription() == null || value.getDescription().length() < 2) {
            errs.put("description", "لطفا توضیح را وارد نمایید");
            isErrored = true;
        }

        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
