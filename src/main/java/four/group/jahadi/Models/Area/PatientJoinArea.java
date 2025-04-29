package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.ObjectIdSerialization;
import four.group.jahadi.Models.Patient;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

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
    private LocalDateTime createdAt;

    private Boolean trained = false;
    private Patient patientInfo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PatientReferral> referrals;
}
