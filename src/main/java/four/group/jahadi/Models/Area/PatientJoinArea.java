package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.Model;
import four.group.jahadi.Models.ObjectIdSerialization;
import four.group.jahadi.Models.Patient;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientJoinArea {

    @Field("_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId id;

    @Field("created_at")
    private Date createdAt;

    private Patient patientInfo;
}
