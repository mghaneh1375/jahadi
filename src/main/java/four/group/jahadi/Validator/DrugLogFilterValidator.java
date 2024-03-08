package four.group.jahadi.Validator;

import four.group.jahadi.DTO.DrugLogFilter;
import org.json.JSONObject;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DrugLogFilterValidator implements ConstraintValidator<ValidatedDrugLogFilter, DrugLogFilter> {

    @Override
    public void initialize(ValidatedDrugLogFilter constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(DrugLogFilter value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if(
            value.getStartAt() != null && value.getEndAt() != null && 
            value.getStartAt() >= value.getEndAt()
        ) {
            errs.put("startAt", "تاریخ شروع بازه باید کوچک تر تاریخ اتمام بازه باشد");
            isErrored = true;
        }

        if(
            value.getStartAt() != null && value.getEndAt() != null && 
            value.getStartAt() >= value.getEndAt()
        ) {
            errs.put("startAt", "تاریخ شروع بازه باید کوچک تر تاریخ اتمام بازه باشد");
            isErrored = true;
        }

        long curr = System.currentTimeMillis();
        if(value.getStartAt() != null && value.getStartAt() >= curr) {
            errs.put("startAt", "تاریخ شروع بازه باید کوچک تر از اکنون باشد");
            isErrored = true;
        }

        if(value.getStartAt() != null && value.getStartAt() < 1709366474000L) {
            errs.put("startAt", "تاریخ شروع بازه معتبر نمی باشد");
            isErrored = true;
        }

        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
