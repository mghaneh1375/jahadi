package four.group.jahadi.DTO.SignUp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.DTO.PersianNumberDeserializer;
import four.group.jahadi.Validator.JustNumeric;
import four.group.jahadi.Validator.SignUp.ValidatedPersonSignUpFormCheckPhone;
import four.group.jahadi.Validator.SignUp.ValidatedSignUpFormStep1;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidatedPersonSignUpFormCheckPhone
public class PersonSignUpCheckPhoneData {
    @JustNumeric
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String phone;
}
