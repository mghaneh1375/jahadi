package four.group.jahadi.Validator;

import four.group.jahadi.DTO.Trip.TripStep1Data;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TripStep1Validator implements ConstraintValidator<ValidatedTripStep1, TripStep1Data> {

    @Override
    public void initialize(ValidatedTripStep1 constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(TripStep1Data value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if(value.getOwner() == null || value.getWriteAccess() == null) {
            errs.put("data", "لطفا مسئول و شماره اردو ها را وارد نمایید");
            isErrored = true;
        }

        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
