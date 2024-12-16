package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.ExternalReferralTrackingStatus;
import four.group.jahadi.Models.DateSerialization;
import four.group.jahadi.Models.Model;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientForm extends Model {

    @Field("sub_module_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId subModuleId;

    @Field("doctor_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId doctorId;

    private List<PatientAnswer> answers;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HashMap<ObjectId, Integer> mark;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    @Field("external_referral_tracking_status")
    private ExternalReferralTrackingStatus externalReferralTrackingStatus;

    @Field("external_referral_tracking_status_last_modified_at")
    @JsonSerialize(using = DateSerialization.class)
    private Date externalReferralTrackingStatusLastModifiedAt;
}
