package four.group.jahadi.Validator;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = UserValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface ValidatedUser {

    String message() default "";

    Class[] groups() default {};

    Class[] payload() default {};

}
