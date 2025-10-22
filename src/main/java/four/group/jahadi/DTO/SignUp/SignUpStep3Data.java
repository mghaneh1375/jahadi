package four.group.jahadi.DTO.SignUp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.DTO.PersianNumberDeserializer;
import four.group.jahadi.Validator.JustNumeric;
import four.group.jahadi.Validator.SignUp.ValidatedSignUpFormStep3;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidatedSignUpFormStep3
public class SignUpStep3Data {

    @Size(min = 3, max = 50)
    private String nearbyName;

    @Size(min = 2, max = 50)
    private String nearbyRel;

    @JustNumeric
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String nearbyPhone;


    @Size(min = 6, max = 50)
    private String password;

    private String passwordRepeat;

    private Integer groupCode;

}
