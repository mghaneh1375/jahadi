package four.group.jahadi.Validator.SignUp;


import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = SignUpFormStep3Validator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface ValidatedSignUpFormStep3 {

    String message() default "";

    Class[] groups() default {};

    Class[] payload() default {};

}
