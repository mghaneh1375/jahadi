package four.group.jahadi.DTO.SignUp;


import four.group.jahadi.Validator.SignUp.ValidatedPasswordRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ValidatedPasswordRequest
public class PasswordData {
    private String currPassword;
    private String password;
    private String repeatPassword;

}
