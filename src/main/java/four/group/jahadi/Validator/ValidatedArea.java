package four.group.jahadi.Validator;


import four.group.jahadi.Validator.SignUp.GroupSignUpFormValidator;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = AreaValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface ValidatedArea {

    String message() default "";

    Class[] groups() default {};

    Class[] payload() default {};

}
