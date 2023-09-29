package four.group.jahadi.Exception;

public class InvalidCodeException extends RuntimeException {

    private static final long serialVersionUID = 1321321321L;

    public InvalidCodeException() {}

    @Override
    public String getMessage() {
        return "کد وارد شده نامعتبر است";
    }
}
