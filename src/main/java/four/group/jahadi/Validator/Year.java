package four.group.jahadi.Validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value={METHOD,FIELD,ANNOTATION_TYPE,CONSTRUCTOR,PARAMETER,TYPE_USE})
@Retention(value=RUNTIME)
@Documented
@Constraint(validatedBy=YearValidator.class)
public @interface Year {

    String message() default "سال وارد شده معتبر نمی باشد";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
