package four.group.jahadi.Validator.SignUp;

import four.group.jahadi.DTO.SignUp.*;
import four.group.jahadi.Enums.GroupRegistrationPlace;
import four.group.jahadi.Enums.Lodgment;
import four.group.jahadi.Utility.Utility;
import four.group.jahadi.Validator.DateValidator;
import four.group.jahadi.Validator.PhoneValidator;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class UpdateInfoValidator implements ConstraintValidator<ValidatedUpdateInfo, UpdateInfoData> {

    @Autowired
    ModelMapper modelMapper;

    @Override
    public void initialize(ValidatedUpdateInfo constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UpdateInfoData value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if (value.getNearbyPhone() != null &&
                !ObjectUtils.isEmpty(value.getNearbyPhone()) &&
                !PhoneValidator.isValid(value.getNearbyPhone())
        ) {
            errs.put("nearbyPhone", "شماره همراه وارد شده معتبر نمی باشد");
            isErrored = true;
        }

        if (value.getBirthDay() != null && (
                ObjectUtils.isEmpty(value.getBirthDay()) ||
                        !DateValidator.isValid(value.getBirthDay()) ||
                        Integer.parseInt(value.getBirthDay().split("\\/")[0]) > 1395)
        ) {
            errs.put("birthDay", "تاریخ تولد وارد شده معتبر نمی باشد");
            isErrored = true;
        }

        if (value.getNearbyPhone() != null && (
                ObjectUtils.isEmpty(value.getNearbyPhone()) || !PhoneValidator.isValid(value.getNearbyPhone())
        )) {
            errs.put("nearbyPhone", "شماره همراه وارد شده معتبر نمی باشد");
            isErrored = true;
        }

        if (value.getNid() != null && (
                ObjectUtils.isEmpty(value.getNid()) || !Utility.validationNationalCode(value.getNid())
        )) {
            errs.put("NID", "کد ملی وارد شده معتبر نمی باشد");
            isErrored = true;
        }

        if(value.getLodgment() != null &&
                Objects.equals(value.getLodgment(), Lodgment.OTHER) &&
                value.getLodgmentOther() == null
        )  {
            errs.put("lodgment", "لطفا محل استقرار گروه را وارد نمایید");
            isErrored = true;
        }

        if(value.getLodgment() != null &&
                !Objects.equals(value.getLodgment(), Lodgment.OTHER) &&
                value.getLodgmentOther() != null
        )  {
            errs.put("lodgment", "محل استقرار گروه نامعتبر است");
            isErrored = true;
        }

        if(value.getGroupRegistrationPlace() != null &&
                Objects.equals(value.getGroupRegistrationPlace(), GroupRegistrationPlace.OTHER) &&
                value.getGroupRegistrationPlaceOther() == null
        ) {
            errs.put("groupRegistrationPlace", "لطفا محل ثبت گروه را وارد نمایید");
            isErrored = true;
        }

        if(value.getGroupRegistrationPlace() != null &&
                !Objects.equals(value.getGroupRegistrationPlace(), GroupRegistrationPlace.OTHER) &&
                value.getGroupRegistrationPlaceOther() != null
        ) {
            errs.put("groupRegistrationPlace", "محل ثبت گروه نامعتبر است");
            isErrored = true;
        }

        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
