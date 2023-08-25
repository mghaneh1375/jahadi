package four.group.jahadi.DTO;

import four.group.jahadi.Enums.Color;
import four.group.jahadi.Validator.ValidatedProject;
import lombok.*;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ValidatedProject
public class ProjectData {

    List<ObjectId> groupIds;

    String name;
    Color color;

    String startAt;
    String endAt;

    List<String> tripNos;
}
