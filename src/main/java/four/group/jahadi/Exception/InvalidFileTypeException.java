package four.group.jahadi.Exception;

public class InvalidFileTypeException extends Exception {

    private static final long serialVersionUID = 5L;

    private final String message;

    public InvalidFileTypeException(String message) {
        this.message = message;
    }

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public String getMessage() {
        return message;
    }

}
