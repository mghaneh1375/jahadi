package four.group.jahadi.Exception;

public class InvalidCodeException extends RuntimeException {

    private static final long serialVersionUID = 1321321321L;

    public InvalidCodeException() {}

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public String getMessage() {
        return "کد وارد شده نامعتبر است";
    }
}
