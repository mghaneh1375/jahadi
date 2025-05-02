package four.group.jahadi.Exception;

public class NotActivateAccountException extends Exception {

    private static final long serialVersionUID = 51L;

    private final String message;

    public NotActivateAccountException(String message) {
        this.message = message;
    }

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public String getMessage() {
        return message;
    }

}
