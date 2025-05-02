package four.group.jahadi.Exception;

public class NotCompleteAccountException extends Exception {

    private static final long serialVersionUID = 52L;

    private final String message;

    public NotCompleteAccountException(String message) {
        this.message = message;
    }

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public String getMessage() {
        return message;
    }

}
