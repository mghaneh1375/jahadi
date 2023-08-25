package four.group.jahadi.Validator;

import four.group.jahadi.DTO.GroupData;
import four.group.jahadi.DTO.ModuleData;
import four.group.jahadi.Enums.Color;
import four.group.jahadi.Models.Section;
import org.json.JSONObject;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GroupValidator implements ConstraintValidator<ValidatedGroup, GroupData> {

    @Override
    public void initialize(ValidatedGroup constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(GroupData value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if (value.getName() == null || value.getName().length() < 3) {
            errs.put("name", "لطفا نام گروه را وارد نمایید (حداقل 3 کاراکتر)");
            isErrored = true;
        }

        if(value.getColor() == null) {
            errs.put("color", "رنگ وارد شده معتبر نمی باشد");
            isErrored = true;
        }

        if(value.getOwner() == null) {
            errs.put("owner", "لطفا مسئول گروه را مشخص نمایید");
            isErrored = true;
        }

        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
