package four.group.jahadi.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Validated
@AllArgsConstructor
@NoArgsConstructor
public class AddPatientExternalReferralsServiceDAO {
    @NotNull
    @Size(min = 3, max = 500, message = "نوع خدمت باید حداقل 3 و حداکثر 500 کاراکتر باشد")
    private String service;
    @NotNull
    private ObjectId formId;
    @NotNull
    private ObjectId patientId;
    @NotNull
    private ObjectId areaId;
    @NotNull
    @Min(value = 0, message = "مقدار هزینه کل باید مثبت باشد")
    private Long totalCost;
    @NotNull
    @Min(value = 0, message = "مقدار پرداخت شده توسط بیمار باید مثبت باشد")
    private Long userPaid;
    @NotNull
    @Min(value = 0, message = "مقدار پرداخت شده توسط جهادی باید مثبت باشد")
    private Long jahadiPaid;
    @NotNull
    @Min(value = 0, message = "مقدار هزینه کسر شده توسط مرکز درمان باید مثبت باشد")
    private Long hospitalCost;
    @NotNull
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;
    @NotNull
    @Size(min = 3, max = 500, message = "محل خدمت باید حداقل 3 و حداکثر 500 کاراکتر باشد")
    private String location;
    @Size(min = 3, max = 1000, message = "توضیحات باید حداقل 3 و حداکثر 500 کاراکتر باشد")
    private String comeDescription;
    @Size(min = 3, max = 1000, message = "توضیحات باید حداقل 3 و حداکثر 500 کاراکتر باشد")
    private String notComingDescription;
    @Min(0)
    @Max(value = 100, message = "تعداد تماس باید کمتر از 100 باشد")
    private Integer comeCallingCount;
    @Min(0)
    @Max(value = 100, message = "تعداد تماس باید کمتر از 100 باشد")
    private Integer notComingCallingCount;
}
