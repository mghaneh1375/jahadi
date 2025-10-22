package four.group.jahadi.DTO.SignUp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.DTO.PersianNumberDeserializer;
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
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String phone;

    private Integer code;

}
