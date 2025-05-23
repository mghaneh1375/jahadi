package four.group.jahadi.Validator;

import four.group.jahadi.DTO.Region.RegionRunInfoData;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class RegionRunInfoValidator implements ConstraintValidator<ValidatedRegionRunInfo, RegionRunInfoData> {

    @Override
    public void initialize(ValidatedRegionRunInfo constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    private static final String latRegex = "^(\\+|-)?((\\d((\\.)|\\.\\d{1,6})?)|(0*?[0-8]\\d((\\.)|\\.\\d{1,6})?)|(0*?90((\\.)|\\.0{1,6})?))$";
    private static final String lngRegex = "^(\\+|-)?((\\d((\\.)|\\.\\d{1,6})?)|(0*?\\d\\d((\\.)|\\.\\d{1,6})?)|(0*?1[0-7]\\d((\\.)|\\.\\d{1,6})?)|(0*?180((\\.)|\\.0{1,6})?))$";

    private static final Pattern latPattern = Pattern.compile(latRegex);
    private static final Pattern lngPattern = Pattern.compile(lngRegex);

    @Override
    public boolean isValid(RegionRunInfoData value, ConstraintValidatorContext context) {

        boolean isErrored = false;
        JSONObject errs = new JSONObject();

        if(value.getCityId() == null || value.getStartAt() == null ||
            value.getEndAt() == null || value.getDailyStartAt() == null ||
            value.getDailyEndAt() == null || value.getLat() == null ||
            value.getLng() == null
        ) {
            errs.put("data", "لطفا تمام اطلاعات را وارد نمایید");
            isErrored = true;
        }

//        if(value.getStartAt() != null && value.getStartAt() < System.currentTimeMillis()) {
//            errs.put("startAt", "تاریخ شروع باید از امروز بزرگ تر باشد");
//            isErrored = true;
//        }

        if(value.getEndAt() != null && (long)value.getStartAt() > (long)value.getEndAt()) {
            errs.put("endAt", "تاریخ اتمام باید از شروع بزرگ تر باشد");
            isErrored = true;
        }

        if(!TimeValidator.isValid(value.getDailyStartAt())) {
            errs.put("dailyStartAt", "زمان شروع وارد شده نامعتبر است");
            isErrored = true;
        }

        if(!TimeValidator.isValid(value.getDailyEndAt())) {
            errs.put("dailyEndAt", "زمان اتمام وارد شده نامعتبر است");
            isErrored = true;
        }

//        if(!latPattern.matcher(value.getLat() + "").matches()) {
//            errs.put("lat", "عرض جغرافیایی وارد شده نامعتبر است");
//            isErrored = true;
//        }
//
//        if(!lngPattern.matcher(value.getLng() + "").matches()) {
//            errs.put("lat", "طول جغرافیایی وارد شده نامعتبر است");
//            isErrored = true;
//        }

        if(!ObjectId.isValid(value.getCityId())) {
            errs.put("cityId", "کد شهر نامعتبر است");
            isErrored = true;
        }

        if(isErrored) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errs.toString())
                    .addConstraintViolation();
        }

        return !isErrored;
    }

}
