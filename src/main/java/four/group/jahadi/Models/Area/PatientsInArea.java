package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.Model;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

import static four.group.jahadi.Utility.Utility.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "patients_in_area")
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer turn;

    private Boolean trained = false;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private List<PatientReferral> referrals;

    @Field("train_form")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TrainForm trainForm;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(this.getId()) +
                ", \"createdAt\":" + printNullableDate(this.getCreatedAt()) +
                ", \"patientId\":" + printNullableField(patientId) +
                ", \"areaId\":" + printNullableField(areaId) +
                ", \"turn\":" + printNullableInteger(turn) +
                ", \"trained\":" + trained +
                ", \"referrals\":" + referrals +
                ", \"trainForm\":" + trainForm +
                "}\n";
    }
}
