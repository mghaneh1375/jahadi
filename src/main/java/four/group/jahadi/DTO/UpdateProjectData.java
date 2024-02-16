package four.group.jahadi.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.Enums.Color;
import four.group.jahadi.Models.ColorDeserialization;
import four.group.jahadi.Validator.ValidatedProject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ValidatedProject
public class UpdateProjectData {

    @Size(min = 2, max = 50)
    String name;

    @JsonDeserialize(using = ColorDeserialization.class)
    Color color;

    Long startAt;
    Long endAt;
}
