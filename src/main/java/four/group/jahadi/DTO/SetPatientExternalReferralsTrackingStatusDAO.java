package four.group.jahadi.DTO;

import four.group.jahadi.Enums.ExternalReferralTrackingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class SetPatientExternalReferralsTrackingStatusDAO {
    private ObjectId areaId;
    private ObjectId formId;
    private ObjectId patientId;
    private ExternalReferralTrackingStatus status;
}
