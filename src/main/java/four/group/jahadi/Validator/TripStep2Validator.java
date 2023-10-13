package four.group.jahadi.Validator;

import four.group.jahadi.DTO.Trip.TripStep2Data;
import org.json.JSONObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TripStep2Validator implements ConstraintValidator<ValidatedTripStep2, TripStep2Data> {

    @Override
    public void initialize(ValidatedTripStep2 constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(TripStep2Data value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if(
                value.getName() == null || value.getStartAt() == null ||
                        value.getEndAt() == null || value.getDailyStartAt() == null ||
                        value.getDailyEndAt() == null
        ) {
            errs.put("data", "لطفا تمام اطلاعات را وارد نمایید");
            isErrored = true;
        }

        if(value.getStartAt() == null) {
            errs.put("startAt", "تاریخ شروع نامعتبر است");
            isErrored = true;
        }

        if(value.getStartAt() != null && value.getStartAt() < System.currentTimeMillis()) {
            errs.put("startAt", "تاریخ شروع باید از امروز بزرگ تر باشد");
            isErrored = true;
        }

        if(value.getEndAt() == null) {
            errs.put("endAt", "تاریخ اتمام نامعتبر است");
            isErrored = true;
        }

        if(value.getEndAt() != null && value.getStartAt() > value.getEndAt()) {
            errs.put("endAt", "تاریخ اتمام باید از شروع بزرگ تر باشد");
            isErrored = true;
        }

        if(value.getName() != null && value.getName().length() < 2) {
            errs.put("name", "لطفا نام را وارد نمایید");
            isErrored = true;
        }

        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
