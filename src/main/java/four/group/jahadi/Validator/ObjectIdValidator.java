package four.group.jahadi.Validator;

import org.bson.types.ObjectId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class ObjectIdValidator implements
        ConstraintValidator<ObjectIdConstraint, ObjectId> {


    @Override
    @four.group.jahadi.Utility.KeepMethodName
    public void initialize(ObjectIdConstraint constraintAnnotation) {

    }

    @Override
    @four.group.jahadi.Utility.KeepMethodName
    public boolean isValid(ObjectId o, ConstraintValidatorContext constraintValidatorContext) {
        return ObjectId.isValid(o.toString());
    }

    public static boolean isValid(String str) {
        return ObjectId.isValid(str);
    }
}
