package four.group.jahadi.Validator;

import four.group.jahadi.DTO.ModuleData;
import four.group.jahadi.Models.Section;
import org.json.JSONObject;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ModuleValidator implements ConstraintValidator<ValidatedModule, ModuleData> {

    @Override
    public void initialize(ValidatedModule constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ModuleData value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if (ObjectUtils.isEmpty(value.getName())) {
            errs.put("name", "لطفا نام بخش را وارد نمایید");
            isErrored = true;
        }

        if(!EnumValidatorImp.isValid(value.getSection(), Section.class)) {
            errs.put("section", "بخش می تواند یا در داخل پزشکی و یا دندان پزشکی باشد");
            isErrored = true;
        }

        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
