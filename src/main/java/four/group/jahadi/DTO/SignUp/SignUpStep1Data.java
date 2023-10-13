package four.group.jahadi.DTO.SignUp;

import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Validator.JustNumeric;
import four.group.jahadi.Validator.SignUp.ValidatedSignUpFormStep1;
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
@ValidatedSignUpFormStep1
public class SignUpStep1Data {

    @Size(min = 3, max = 50)
    private String name;

    @Size(min = 3, max = 50)
    private String fatherName;

    private String birthDay;

    @Size(min = 3, max = 50)
    private String field;

    @Size(min = 3, max = 50)
    private String university;

    @Year
    private String universityYear;

    @JustNumeric
    private String nid;

    @JustNumeric
    private String phone;

    private Sex sex;

}
