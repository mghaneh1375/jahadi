package four.group.jahadi.DTO.SignUp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

    private String token;
    private String nid;
    private Integer code;
    private String password;
    private String repeatPassword;

}
