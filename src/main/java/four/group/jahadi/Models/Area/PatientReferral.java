package four.group.jahadi.Models.Area;


import com.fasterxml.jackson.annotation.JsonInclude;
import four.group.jahadi.Models.Model;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientReferral extends Model {

    @Field("module_id")
    private ObjectId moduleId;

    private boolean recepted = false;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private List<PatientForm> forms;

}
