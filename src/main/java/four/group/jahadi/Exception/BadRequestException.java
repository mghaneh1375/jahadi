package four.group.jahadi.Exception;

public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 52L;

    public BadRequestException() {}

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public String getMessage() {
        return "درخواست شما نامعتبر است";
    }
}
