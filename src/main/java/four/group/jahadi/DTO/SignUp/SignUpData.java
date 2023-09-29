package four.group.jahadi.DTO.SignUp;

import four.group.jahadi.Enums.BloodType;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Validator.SignUp.ValidatedSignUpForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidatedSignUpForm
public class SignUpData {

    private String name;
    private String fatherName;
    private String birthDay;
    private Integer universityYear;
    private String field;
    private String university;
    private String Nid;
    private String phone;
    private Sex sex;
    private String abilities;
    private String diseases;
    private String allergies;
    private BloodType bloodType;
    private String nearbyName;
    private String nearbyPhone;
    private Integer groupCode;
    private String password;
    private String passwordRepeat;

}
