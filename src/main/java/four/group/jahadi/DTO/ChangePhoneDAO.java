package four.group.jahadi.DTO;

import four.group.jahadi.Validator.JustNumeric;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePhoneDAO {
    @NonNull
    @Pattern(regexp = "09[0-9]{9}")
    private String newPhone;
}
