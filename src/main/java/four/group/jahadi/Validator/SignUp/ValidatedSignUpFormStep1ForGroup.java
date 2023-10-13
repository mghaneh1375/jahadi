package four.group.jahadi.Validator.SignUp;


import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = SignUpFormStep1ForGroupValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface ValidatedSignUpFormStep1ForGroup {

    String message() default "";

    Class[] groups() default {};

    Class[] payload() default {};

}
