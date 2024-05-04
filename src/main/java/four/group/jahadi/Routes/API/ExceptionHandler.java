package four.group.jahadi.Routes.API;

import four.group.jahadi.Exception.*;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static four.group.jahadi.Utility.Utility.printException;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = InvalidIdException.class)
    public ResponseEntity<Object> exception(InvalidIdException exception) {
        return new ResponseEntity<>(new JSONObject().put("status", "nok").put("msg", exception.getMessage()).toString(), HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = NotAccessException.class)
    public ResponseEntity<Object> exception(NotAccessException exception) {
        return new ResponseEntity<>(new JSONObject().put("status", "nok").put("msg", exception.getMessage()).toString(), HttpStatus.FORBIDDEN);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> exception(BadRequestException exception) {
        return new ResponseEntity<>(new JSONObject().put("status", "nok").put("msg", exception.getMessage()).toString(), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = InvalidCodeException.class)
    public ResponseEntity<Object> exception(InvalidCodeException exception) {
        return new ResponseEntity<>(new JSONObject().put("status", "nok").put("msg", exception.getMessage()).toString(), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = InvalidFieldsException.class)
    public ResponseEntity<Object> exception(InvalidFieldsException exception) {
        return new ResponseEntity<>(new JSONObject().put("status", "nok").put("msg", exception.getMessage()).toString(), HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        StringBuilder errors = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors())
            errors.append(error.getField()).append(": ").append(error.getDefaultMessage());

        for (ObjectError error : ex.getBindingResult().getGlobalErrors())
            errors.append(error.getDefaultMessage());

        headers.add("Content-Type", "application/json");
        JSONObject errMsg;

        try {
            errMsg = new JSONObject(errors.toString());
        }
        catch (Exception x) {
            String[] tmp = errors.toString().split(":");
            errMsg = new JSONObject().put(tmp[0], tmp[1]);
        }

        return new ResponseEntity<>(
                new JSONObject()
                        .put("status", "nok")
                        .put("fields", errMsg).toString(),
                headers,
                HttpStatus.NOT_ACCEPTABLE);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        String error = ex.getParameterName() + " parameter is missing";
        headers.add("Content-Type", "application/json");

        return new ResponseEntity<>(
                new JSONObject().put("status", "nok").put("msg", error).toString(),
                headers,
                HttpStatus.NOT_ACCEPTABLE
        );
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        String error = ex.getVariableName()+ " parameter is missing";
        headers.add("Content-Type", "application/json");

        return new ResponseEntity<>(
                new JSONObject().put("status", "nok").put("msg", error).toString(),
                headers,
                HttpStatus.NOT_ACCEPTABLE
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        String error =
                ex.getName() + " should be of type " + ex.getRequiredType().getName();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        return new ResponseEntity<>(
                new JSONObject().put("status", "nok").put("msg", error).toString(), headers, HttpStatus.NOT_ACCEPTABLE
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(
                " method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

        return new ResponseEntity<>(
                new JSONObject().put("status", "nok").put("message", builder.toString()).toString(),
                new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        return new ResponseEntity<>(new JSONObject().put("status", "nok").put("msg", error).toString(),
                new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        if(ex != null && ex.getStackTrace().length > 0 && ex.getStackTrace()[0].toString().contains("beanvalidation"))
            return new ResponseEntity<>(
                    new JSONObject().put("status", "nok").put("msg", ex.getLocalizedMessage()).toString(),
                    headers,
                    HttpStatus.OK
            );

        if(ex != null && ex.getMessage() != null &&
                (
                        ex.getMessage().equals("Account not complete") ||
                                ex.getMessage().equals("Account not activated") ||
                                ex.getMessage().equals("Token is not valid") ||
                                ex.getMessage().equals("Access denied")
                )
        ) {

            return new ResponseEntity<>(
                    new JSONObject().put("status", "nok").put("msg", ex.getLocalizedMessage()).toString(),
                    headers,
                    HttpStatus.UNAUTHORIZED);
        }


        if(ex != null) {

//            if(ex.getMessage() != null &&
//                    !ex.getMessage().contains("Invalid json"))
            printException(ex);

            return new ResponseEntity<>(
                    new JSONObject().put("status", "nok").put("msg", ex.getLocalizedMessage()).toString(),
                    headers,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(
                new JSONObject().put("status", "nok").put("msg", "Unknown err").toString(),
                headers,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
