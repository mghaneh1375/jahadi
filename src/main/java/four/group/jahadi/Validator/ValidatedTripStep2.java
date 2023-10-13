package four.group.jahadi.Validator;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = TripStep2Validator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface ValidatedTripStep2 {

    String message() default "";

    Class[] groups() default {};

    Class[] payload() default {};

}
