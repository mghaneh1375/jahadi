package four.group.jahadi.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.DTO.Trip.TripStep1Data;
import four.group.jahadi.Enums.Color;
import four.group.jahadi.Models.ColorDeserialization;
import four.group.jahadi.Validator.ValidatedProject;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ValidatedProject
public class ProjectData {

    @Size(min = 2, max = 50)
    String name;

    @JsonDeserialize(using = ColorDeserialization.class)
    Color color;

    Long startAt;
    Long endAt;

    @NotNull
    @Size(min = 1)
    List<List<TripStep1Data>> trips;
}
