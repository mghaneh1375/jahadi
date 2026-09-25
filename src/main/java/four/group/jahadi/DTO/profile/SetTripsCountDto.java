package four.group.jahadi.DTO.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class SetTripsCountDto {

    @NotNull
    @Min(0)
    @Max(1000)
    private Integer tripsCount;
}
