package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.Drug.AmountOfUse;
import four.group.jahadi.Enums.Drug.HowToUse;
import four.group.jahadi.Enums.Drug.UseTime;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.annotation.Transient;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "patient_drug")
@Builder
public class PatientDrug extends Model {
    @Field("area_id")
    @JsonIgnore
    private ObjectId areaId;
    @Field("patient_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId patientId;
    @Field("module_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private ObjectId moduleId;
    @Field("module_name")
    private String moduleName;
    @Field("giver_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private ObjectId giverId;
    @Field("doctor_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private ObjectId doctorId;
    @Field("drug_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private ObjectId drugId;
    @Field("drug_name")
    private String drugName;
    @Field("suggest_count")
    private Integer suggestCount;
    @Field("give_count")
    private Integer giveCount;
    private boolean dedicated = false;
    @Field("how_to_use")
    private HowToUse howToUse;
    @Field("amount_of_use")
    private AmountOfUse amountOfUse;
    @Field("use_time")
    private UseTime useTime;
    @Field("give_at")
    @JsonSerialize(using = DateSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date giveAt;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String description;
    @Field("give_description")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String giveDescription;
    @Field("given_drug_id")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private ObjectId givenDrugId;
    @Field("given_drug_name")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String givenDrugName;
    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Patient patient;
    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String doctor;
    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String giver;
    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String howToUseFa;
    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String amountOfUseFa;
    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String useTimeFa;
}
