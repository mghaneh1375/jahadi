package four.group.jahadi.DTO.SignUp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.DTO.PersianNumberDeserializer;
import four.group.jahadi.Validator.JustNumeric;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class UniquenessValidatorData {
    @NotNull
    @JustNumeric
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String nid;

    @NotNull
    @JustNumeric
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String phone;
}
