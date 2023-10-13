package four.group.jahadi.DTO;

import four.group.jahadi.DTO.Trip.TripStep1Data;
import four.group.jahadi.Enums.Color;
import four.group.jahadi.Validator.ValidatedProject;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ValidatedProject
public class ProjectData {

    String name;
    Color color;

    Long startAt;
    Long endAt;

    List<TripStep1Data> trips = null;
}
