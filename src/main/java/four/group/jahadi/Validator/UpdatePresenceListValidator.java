package four.group.jahadi.Validator;

import four.group.jahadi.DTO.UpdatePresenceList;
import four.group.jahadi.Utility.Utility;
import org.json.JSONObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UpdatePresenceListValidator implements ConstraintValidator<ValidatedUpdatePresenceList, UpdatePresenceList> {

    @Override
    public void initialize(ValidatedUpdatePresenceList constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UpdatePresenceList value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if(value.getEntrance() == null && value.getExit() == null && (
                value.getJustSetExit() == null || !value.getJustSetExit()
        )) {
            errs.put("entrance", "لطفا زمان ورود یا خروج را وارد نمایید");
            isErrored = true;
        }

        if((value.getJustSetExit() == null || !value.getJustSetExit()) &&
                value.getEntrance() != null && value.getExit() !=null &&
                Utility.isUtcBefore(value.getExit(), value.getEntrance())
        ) {
            errs.put("entrance", "زمان ورود باید قبل از زمان خروج باشد");
            isErrored = true;
        }

        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
