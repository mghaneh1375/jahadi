package four.group.jahadi.Exception;

import java.util.function.Supplier;

public class InvalidIdException extends RuntimeException {

    private static final long serialVersionUID = 1321321321L;

    public InvalidIdException() {}

    @Override
    public String getMessage() {
        return "شناسه وارد شده نامعتبر است";
    }
}
