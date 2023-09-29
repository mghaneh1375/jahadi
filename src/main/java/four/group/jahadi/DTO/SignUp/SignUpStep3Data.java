package four.group.jahadi.DTO.SignUp;

import four.group.jahadi.Validator.SignUp.ValidatedSignUpFormStep2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidatedSignUpFormStep2
public class SignUpStep3Data {

    private String nearbyName;
    private String nearbyPhone;

    private String password;
    private String passwordRepeat;

    private Integer groupCode;

}
