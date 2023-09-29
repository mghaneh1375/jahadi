package four.group.jahadi.Exception;

public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 52L;

    public BadRequestException() {}

    @Override
    public String getMessage() {
        return "درخواست شما نامعتبر است";
    }
}
