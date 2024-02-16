package four.group.jahadi.Validator;

import four.group.jahadi.DTO.ProjectData;
import org.json.JSONObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static four.group.jahadi.Utility.StaticValues.ONE_DAY_MSEC;

public class ProjectValidator implements ConstraintValidator<ValidatedProject, ProjectData> {

    @Override
    public void initialize(ValidatedProject constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ProjectData value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if (value.getName() == null) {
            errs.put("name", "لطفا نام پروژه را وارد نمایید (حداقل 3 کاراکتر)");
            isErrored = true;
        }

        if(value.getColor() == null) {
            errs.put("color", "رنگ وارد شده معتبر نمی باشد");
            isErrored = true;
        }

        if(value.getStartAt() == null) {
            errs.put("startAt", "تاریخ شروع نامعتبر است");
            isErrored = true;
        }

        if(value.getStartAt() != null && value.getStartAt() < System.currentTimeMillis() - ONE_DAY_MSEC) {
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

        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
