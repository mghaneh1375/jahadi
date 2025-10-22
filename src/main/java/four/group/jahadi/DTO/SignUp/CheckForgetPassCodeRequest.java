package four.group.jahadi.DTO.SignUp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.DTO.PersianNumberDeserializer;
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
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String nid;

    private Integer code;

}
