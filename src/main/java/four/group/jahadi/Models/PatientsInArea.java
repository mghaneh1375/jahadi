package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientsInArea extends Model {

    @Field("patient_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId patientId;

    @Field("area_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId areaId;

    @Field("has_insurance")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean hasInsurance;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer turn;


}
