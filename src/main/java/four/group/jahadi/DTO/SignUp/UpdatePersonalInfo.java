package four.group.jahadi.DTO.SignUp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.DTO.PersianNumberDeserializer;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Validator.JustNumeric;
import four.group.jahadi.Validator.Year;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePersonalInfo {

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    @Size(min = 3, max = 50)
    private String fatherName;

    @NotBlank
    private String birthDay;

    @Year
    private String universityYear;

    @Year
    private String endManageYear;

    @NotBlank
    @Size(min = 3, max = 50)
    private String field;

    @NotBlank
    @Size(min = 3, max = 50)
    private String university;

    @NotNull
    @JustNumeric
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String nid;

    @NotNull
    @JustNumeric
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String cid;

    @NotNull
    private Sex sex;
}
