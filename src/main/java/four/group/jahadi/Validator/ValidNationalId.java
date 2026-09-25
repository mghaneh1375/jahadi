package four.group.jahadi.Validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NationalIdValidatorImpl.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNationalId {
    String message() default "کدملی وارد شده نامعتبر است";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
