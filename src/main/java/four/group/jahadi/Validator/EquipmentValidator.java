package four.group.jahadi.Validator;

import four.group.jahadi.DTO.EquipmentData;
import four.group.jahadi.Enums.EquipmentType;
import org.json.JSONObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EquipmentValidator implements ConstraintValidator<ValidatedEquipment, EquipmentData> {

    @Override
    public void initialize(ValidatedEquipment constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(EquipmentData value, ConstraintValidatorContext context) {

        JSONObject errs = new JSONObject();

        if(value.getEquipmentType() != null) {
            if(value.getEquipmentType().equals(EquipmentType.INFRASTRUCTURE) &&
                    value.getGuaranteeExpireAt() == null
            )
                errs.put("guaranteeAt", "تاریخ اتمام گارانتی را وارد نمایید");

            if(value.getEquipmentType().equals(EquipmentType.INFRASTRUCTURE) &&
                    value.getPropertyId() == null
            )
                errs.put("propertyId", "شماره اموار را وارد نمایید");
        }

        if(errs.keySet().size() > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
            return false;
        }

        return true;
    }

}
