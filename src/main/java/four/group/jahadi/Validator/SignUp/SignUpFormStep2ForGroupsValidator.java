package four.group.jahadi.Validator.SignUp;

import four.group.jahadi.DTO.SignUp.SignUpStep2ForGroupData;
import four.group.jahadi.Enums.GroupRegistrationPlace;
import four.group.jahadi.Enums.Lodgment;
import org.json.JSONObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class SignUpFormStep2ForGroupsValidator implements ConstraintValidator<ValidatedSignUpFormStep2ForGroups, SignUpStep2ForGroupData> {

    @Override
    public void initialize(ValidatedSignUpFormStep2ForGroups constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SignUpStep2ForGroupData value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if(value.getGroupName() == null) {
            errs.put("groupName", "لطفا نام گروه خود را وارد نمایید");
            isErrored = true;
        }

        if(value.getTotalTrips() == null) {
            errs.put("totalTrips", "لطفا تعداد کل اردوهای جهادی خود را وارد نمایید");
            isErrored = true;
        }

        if(value.getRecentTrips() == null) {
            errs.put("recentTrips", "لطفا تعداد اردوهای جهادی سال اخیر خود را وارد نمایید");
            isErrored = true;
        }

        if(value.getRecentMembers() == null) {
            errs.put("recentMembers", "لطفا تعداد جهادگران سال اخیر خود را وارد نمایید");
            isErrored = true;
        }

        if(value.getTotalMembers() == null) {
            errs.put("totalMembers", "لطفا تعداد کل جهادگران خود را وارد نمایید");
            isErrored = true;
        }

        if(value.getManagersCount() == null) {
            errs.put("managersCount", "لطفا تعداد افراد مدیریتی خود را وارد نمایید");
            isErrored = true;
        }

        if(value.getMembersPerTrip() == null) {
            errs.put("managersCount", "لطفا تعداد نفر اردو خود را وارد نمایید");
            isErrored = true;
        }

        if(value.getAtlasCode() == null) {
            errs.put("atlasCode", "لطفا کد اطلس خود را وارد نمایید");
            isErrored = true;
        }

        if(value.getAddress() == null) {
            errs.put("address", "لطفا نشانی پایگاه را وارد نمایید");
            isErrored = true;
        }

        if(value.getTel() == null || value.getTel().length() < 5) {
            errs.put("tel", "لطفا تلفن ثابت را وارد نمایید");
            isErrored = true;
        }

        if(value.getEstablishYear() == null) {
            errs.put("establishYear", "لطفا سال تاسیس را وارد نمایید");
            isErrored = true;
        }

        if(value.getLodgment() == null) {
            errs.put("lodgment", "لطفا محل استقرار گروه را وارد نمایید");
            isErrored = true;
        }

        if(value.getGroupRegistrationPlace() == null) {
            errs.put("groupRegistrationPlace", "لطفا محل ثبت گروه را وارد نمایید");
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

        if(value.getTripFrequency() == null) {
            errs.put("tripFrequency", "لطفا بسامد برگزاری اردوها را وارد نمایید");
            isErrored = true;
        }

        if(value.getTripRadius() == null) {
            errs.put("tripRadius", "لطفا شعاع برگزاری اردو را وارد نمایید");
            isErrored = true;
        }

        if(value.getPlatform() == null) {
            errs.put("platform", "لطفا پلتفرم را وارد نمایید");
            isErrored = true;
        }

        if(value.getTripDays() == null) {
            errs.put("tripDays", "لطفا تعداد روز اردو را وارد نمایید");
            isErrored = true;
        }

        if(value.getRegionsCount() == null) {
            errs.put("regionsCount", "لطفا تعداد مناطق را وارد نمایید");
            isErrored = true;
        }


        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
