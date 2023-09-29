package four.group.jahadi.DTO.SignUp;

import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Validator.SignUp.ValidatedSignUpFormStep1;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidatedSignUpFormStep1
public class SignUpStep1Data {

    private String name;
    private String fatherName;
    private String birthDay;
    private String field;
    private String university;
    private Integer universityYear;
    private String Nid;
    private String phone;
    private Sex sex;

}
