package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.Experiment;
import four.group.jahadi.Models.Model;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientExperiment extends Model {

    @Field("doctor_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId doctorId;
    private Experiment experiment;
    @Size(max = 500)
    private String description;
}
