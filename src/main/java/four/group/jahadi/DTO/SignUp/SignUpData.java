package four.group.jahadi.DTO.SignUp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.DTO.PersianNumberDeserializer;
import four.group.jahadi.Enums.BloodType;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Validator.JustNumeric;
import four.group.jahadi.Validator.SignUp.ValidatedSignUpForm;
import four.group.jahadi.Validator.Year;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidatedSignUpForm
public class SignUpData {

    @JustNumeric
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String phone;

    private String abilities;
    private String diseases;
    private String allergies;
    private BloodType bloodType;

    @Size(min = 3, max = 50)
    private String nearbyName;

    @Size(min = 2, max = 50)
    private String nearbyRel;

    @JustNumeric
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String nearbyPhone;

    private Integer groupCode;

    @Size(min = 6, max = 50)
    private String password;
    private String passwordRepeat;

}
