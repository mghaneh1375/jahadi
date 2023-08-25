package four.group.jahadi.DTO;

import four.group.jahadi.Enums.Color;
import four.group.jahadi.Validator.ValidatedUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ValidatedUser
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserData {

    private String firstName;
    private String lastName;
    private String password;
    private Color color;
    private String sex;
    private String nid;
    private String phone;
    private String educationalField;
    private String birthDay;
    private String specification;
}
