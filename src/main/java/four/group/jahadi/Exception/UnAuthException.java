package four.group.jahadi.Exception;

public class UnAuthException extends Exception {

    private static final long serialVersionUID = 54L;

    private final String message;

    public UnAuthException(String message) {
        this.message = message;
    }

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public String getMessage() {
        return message;
    }

}
