package four.group.jahadi.Models.Area;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.DateSerialization;
import four.group.jahadi.Models.Model;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientReferral extends Model {

    @Field("module_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId moduleId;

    private boolean recepted = false;
    private String desc;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private List<PatientForm> forms;

    @Field("recepted_at")
    @JsonSerialize(using = DateSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date receptedAt;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String moduleName;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String moduleTabName;
}
