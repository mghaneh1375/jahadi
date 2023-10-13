package four.group.jahadi.DTO.SignUp;

import four.group.jahadi.Enums.BloodType;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Validator.SignUp.ValidatedSignUpForm;
import four.group.jahadi.Validator.SignUp.ValidatedUpdateInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidatedUpdateInfo
public class UpdateInfoData {

    private String name;
    private String fatherName;
    private String birthDay;
    private String universityYear;
    private String field;
    private String university;
    private Sex sex;
    private String abilities;
    private String diseases;
    private String allergies;
    private BloodType bloodType;
    private String nearbyName;
    private String nearbyPhone;

}
