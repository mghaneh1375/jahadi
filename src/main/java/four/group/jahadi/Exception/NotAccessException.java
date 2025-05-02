package four.group.jahadi.Exception;

public class NotAccessException extends RuntimeException {

    private static final long serialVersionUID = 58L;

    public NotAccessException() {}

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public String getMessage() {
        return "شما اجازه دسترسی به این امکان را ندارید";
    }

}
