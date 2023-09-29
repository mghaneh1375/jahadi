package four.group.jahadi.Validator.SignUp;

import four.group.jahadi.DTO.SignUp.SignUpStep2ForGroupData;
import org.json.JSONObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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

        if(value.getTrips() == null) {
            errs.put("trips", "لطفا تعداد اردوهای جهادی خود را وارد نمایید");
            isErrored = true;
        }

        if(value.getMembers() == null) {
            errs.put("members", "لطفا تعداد جهادگران خود را وارد نمایید");
            isErrored = true;
        }

        if(value.getFamiliarWith() == null) {
            errs.put("familiarWith", "لطفا نحوه آشنایی خود با نرم افزار ما را وارد نمایید");
            isErrored = true;
        }

        if(value.getOrganizationDependency() == null) {
            errs.put("organizationDependency", "لطفا وضعیت انتساب به دانشگاه یا سازمانی خود را وارد نمایید");
            isErrored = true;
        }

        if(
                value.getPasswordRepeat() == null ||
                        value.getPassword() == null
        ) {
            errs.put("data", "لطفا رمزعبور و تکرار آن را وارد نمایید");
            isErrored = true;
        }

        if(!value.getPasswordRepeat().equals(value.getPassword())) {
            errs.put("password", "رمزعبور وارد شده با تکرار آن یکسان نیست");
            isErrored = true;
        }

        if(value.getPassword().length() < 6) {
            errs.put("password", "رمزعبور وارد شده معتبر نمی باشد");
            isErrored = true;
        }


        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString()).addConstraintViolation();
        }

        return !isErrored;
    }

}
