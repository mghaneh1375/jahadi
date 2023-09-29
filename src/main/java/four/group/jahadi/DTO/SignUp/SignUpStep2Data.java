package four.group.jahadi.DTO.SignUp;

import four.group.jahadi.Enums.BloodType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpStep2Data {

    private String abilities;
    private String diseases;
    private String allergies;
    private BloodType bloodType;

}
