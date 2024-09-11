package four.group.jahadi.Models;

import four.group.jahadi.Enums.Drug.AmountOfUse;
import four.group.jahadi.Enums.Drug.HowToUse;
import four.group.jahadi.Enums.Drug.UseTime;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "patient_drug")
@Builder
public class PatientDrug extends Model {
    @Field("area_id")
    private ObjectId areaId;
    @Field("patient_id")
    private ObjectId patientId;
    @Field("doctor_id")
    private ObjectId doctorId;
    @Field("drug_id")
    private ObjectId drugId;
    private Integer amount;
    private boolean delivered = false;
    @Field("how_to_use")
    private HowToUse howToUses;
    @Field("amount_of_use")
    private AmountOfUse amountOfUses;
    @Field("use_time")
    private UseTime useTimes;
}
