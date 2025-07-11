package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Utility.Utility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder
@Document(collection = "external_referral_service")
@NoArgsConstructor
@AllArgsConstructor
public class ExternalReferralService extends Model {

    @Field("patient_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ObjectId patientId;

    @Field("form_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ObjectId formId;

    @Field("area_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ObjectId areaId;

    @Field("module_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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
    private LocalDateTime date;
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

    public void fillExcelRow(Row row, AtomicInteger counter) {
        row.createCell(counter.getAndIncrement()).setCellValue("نام سرویس ارائه شده: " + (service == null ? "" : service));
        row.createCell(counter.getAndIncrement()).setCellValue("کل هزینه اعلامی: " + (totalCost == null ? 0 : totalCost));
        row.createCell(counter.getAndIncrement()).setCellValue("مقدار پرداختی توسط بیمار: " + (userPaid == null ? 0 : userPaid));
        row.createCell(counter.getAndIncrement()).setCellValue("هزینه بیمارستان: " + (hospitalCost == null ? 0 : hospitalCost));
        row.createCell(counter.getAndIncrement()).setCellValue("تاریخ ارائه سرویس: " + (date == null ? "" : Utility.convertUTCDateToJalali(date)));
        row.createCell(counter.getAndIncrement()).setCellValue("محل خدمت: " + (location == null ? "" : location));
        row.createCell(counter.getAndIncrement()).setCellValue("توضیح خدمت: " + (comeDescription == null ? notComingDescription == null ? "" : notComingDescription : comeDescription));
        row.createCell(counter.getAndIncrement()).setCellValue("تعداد تماس پیگیری خدمت: " + (comeCallingCount == null ? notComingCallingCount == null ? "" : notComingCallingCount : comeCallingCount));
    }
}
