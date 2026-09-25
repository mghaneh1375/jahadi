package four.group.jahadi.DTO.reporter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.DTO.PersianNumberDeserializer;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Validator.JustNumeric;
import four.group.jahadi.Validator.StrongPassword;
import four.group.jahadi.Validator.ValidNationalId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReporterUser {

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank
    @StrongPassword
    private String password;

    @NotBlank
    @StrongPassword
    private String passwordRepeat;

    @NotNull
    @JustNumeric
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    @Pattern(regexp = "09\\d{9}")
    private String phone;

    @NotNull
    @JustNumeric
    @ValidNationalId
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String nationalId;

    @NotBlank
    @Size(min = 2, max = 100)
    private String fatherName;

    @NotNull
    @JustNumeric
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String cid;

    @NotNull
    private Sex sex;

    @NotBlank
    private String birthDay;
}
