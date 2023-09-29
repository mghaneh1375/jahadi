package four.group.jahadi.DTO.SignUp;

import four.group.jahadi.Validator.SignUp.ValidatedSignUpFormStep2ForGroups;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidatedSignUpFormStep2ForGroups
public class SignUpStep2ForGroupData {

    private String groupName;
    private String organizationDependency;
    private String trips;
    private Integer members;
    private String familiarWith;
    private String password;
    private String passwordRepeat;

}
