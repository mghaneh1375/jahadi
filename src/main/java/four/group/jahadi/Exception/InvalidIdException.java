package four.group.jahadi.Exception;

public class InvalidIdException extends RuntimeException {

    private static final long serialVersionUID = 1321321321L;

    public InvalidIdException() {}

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public String getMessage() {
        return "شناسه وارد شده نامعتبر است";
    }
}
