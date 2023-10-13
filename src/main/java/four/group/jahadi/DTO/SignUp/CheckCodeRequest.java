package four.group.jahadi.DTO.SignUp;

import four.group.jahadi.Validator.JustNumeric;
import four.group.jahadi.Validator.SignUp.ValidatedCheckCodeRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ValidatedCheckCodeRequest
public class CheckCodeRequest {

    private String token;

    @JustNumeric
    private String phone;

    private Integer code;

}
