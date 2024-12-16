package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Document(collection = "external_referral_service")
@NoArgsConstructor
@AllArgsConstructor
public class ExternalReferralService extends Model {

    @Field("patient_id")
    @JsonIgnore
    private ObjectId patientId;

    @Field("form_id")
    @JsonIgnore
    private ObjectId formId;

    @Field("area_id")
    @JsonIgnore
    private ObjectId areaId;

    @Field("module_id")
    @JsonIgnore
    private ObjectId moduleId;

    @Field("created_by")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId createdBy;

    private String service;
    @Field("total_cost")
    private Long totalCost;
    @Field("user_paid")
    private Long userPaid;
    @Field("jahadi_paid")
    private Long jahadiPaid;
    private Date date;
    private String location;
    @Field("hospital_cost")
    private Long hospitalCost;
    @Field("come_description")
    private String comeDescription;
    @Field("not_coming_description")
    private String notComingDescription;
    @Field("come_calling_count")
    private Integer comeCallingCount;
    @Field("not_coming_calling_count")
    private Integer notComingCallingCount;
    @Builder.Default
    private List<PaymentsToPatient> payments = new ArrayList<>();
    @Builder.Default
    private List<CallHistory> callHistories = new ArrayList<>();
    @Transient
    private User creator;
}
