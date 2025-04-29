package four.group.jahadi.DTO;

import four.group.jahadi.Enums.EquipmentHealthStatus;
import four.group.jahadi.Enums.EquipmentType;
import four.group.jahadi.Validator.ValidatedEquipment;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ValidatedEquipment
public class EquipmentData {
    @NotNull
    private EquipmentType equipmentType;
    @NotNull
    @Size(min = 2, max = 100, message = "نام تجهیزات باید حداقل 2 و حداکثر 100 کاراکتر باشد")
    private String name;
    @NotNull
    @Size(min = 2, max = 100, message = "شرکت سازنده باید حداقل 2 و حداکثر 100 کاراکتر باشد")
    private String producer;
    @NotNull
    @Min(value = 0, message = "مقدار موجودی باید حداقل 0 باشد")
    @Max(value = 100000, message = "مقدار موجودی باید حداکثر 100000 باشد")
    private Integer available;
    @NotNull
    private LocalDateTime buyAt;
    private LocalDateTime usedAt;
    private LocalDateTime guaranteeExpireAt;
    @NotNull
    private EquipmentHealthStatus healthStatus;
    @NotNull
    @Size(min = 1, max = 100, message = "شماره ردیف باید حداقل 2 و حداکثر 100 کاراکتر باشد")
    private String rowNo;
    @NotNull
    @Size(min = 1, max = 100, message = "شماره قفسه باید حداقل 2 و حداکثر 100 کاراکتر باشد")
    private String shelfNo;
    @NotNull
    @Size(min = 2, max = 1000, message = "محل انبار باید حداقل 2 و حداکثر 1000 کاراکتر باشد")
    private String location;
    @Size(min = 2, max = 1000, message = "توضیحات باید حداقل 2 و حداکثر 1000 کاراکتر باشد")
    private String description;
    @Size(min = 2, max = 100, message = "شماره اموال باید حداقل 2 و حداکثر 100 کاراکتر باشد")
    private String propertyId;
}
