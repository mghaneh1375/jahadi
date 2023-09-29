package four.group.jahadi.DTO.SignUp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckCodeRequest {

    private String token;
    private String nid;
    private Integer code;

}
