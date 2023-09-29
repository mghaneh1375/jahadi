package four.group.jahadi.DTO.SignUp;

import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Validator.SignUp.ValidatedGroupSignUpForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidatedGroupSignUpForm
public class GroupSignUpData {

    private String name;
    private String fatherName;
    private String birthDay;
    private Integer universityYear;
    private String field;
    private String university;
    private String Nid;
    private String phone;
    private Sex sex;

    private String groupName;
    private String organizationDependency;
    private String trips;
    private Integer members;
    private String familiarWith;

    private String password;
    private String passwordRepeat;

}
