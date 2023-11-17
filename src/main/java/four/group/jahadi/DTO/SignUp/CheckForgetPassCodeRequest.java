package four.group.jahadi.DTO.SignUp;

import four.group.jahadi.Validator.JustNumeric;
import four.group.jahadi.Validator.SignUp.ValidatedCheckCodeRequest;
import four.group.jahadi.Validator.SignUp.ValidatedCheckForgetPassCodeRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ValidatedCheckForgetPassCodeRequest
public class CheckForgetPassCodeRequest {

    private String token;

    @JustNumeric
    private String nid;

    private Integer code;

}
