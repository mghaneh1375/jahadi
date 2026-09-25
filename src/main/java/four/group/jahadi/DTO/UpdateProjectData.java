package four.group.jahadi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectData {

    @NotBlank
    @Size(min = 2, max = 50)
    String name;

    @NotNull
    Long startAt;

    @NotNull
    Long endAt;
}
