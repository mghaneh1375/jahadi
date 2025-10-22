package four.group.jahadi.DTO.SignUp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.DTO.PersianNumberDeserializer;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Validator.JustNumeric;
import four.group.jahadi.Validator.SignUp.ValidatedSignUpFormStep1ForGroup;
import four.group.jahadi.Validator.Year;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidatedSignUpFormStep1ForGroup
public class SignUpStep1ForGroupData {

    @Size(min = 3, max = 50)
    private String name;

    @Size(min = 3, max = 50)
    private String fatherName;

    private String birthDay;

    @Year
    private String universityYear;

    @Year
    private String endManageYear;

    @Size(min = 3, max = 50)
    private String field;

    @Size(min = 3, max = 50)
    private String university;

    @JustNumeric
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String nid;

    @JustNumeric
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String cid;

    @JustNumeric
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String phone;

    private Sex sex;

    @Size(min = 6, max = 50)
    private String password;

    @Size(min = 6, max = 50)
    private String passwordRepeat;
}
