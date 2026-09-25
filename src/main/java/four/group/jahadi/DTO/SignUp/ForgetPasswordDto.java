package four.group.jahadi.DTO.SignUp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class ForgetPasswordDto {
    @NotNull
    private String nid;
}
