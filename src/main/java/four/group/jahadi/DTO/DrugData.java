package four.group.jahadi.DTO;

import four.group.jahadi.Enums.Drug.DrugLocation;
import four.group.jahadi.Enums.Drug.DrugType;
import four.group.jahadi.Validator.ValidatedDrug;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ValidatedDrug
public class DrugData {

    @NotNull
    @Size(min = 2, max = 100, message = "نام باید حداقل 2 و حداکثر 100 کاراکتر باشد")
    private String name;

    @NotNull
    @Size(min = 2, max = 100, message = "شرکت سازنده باید حداقل 2 و حداکثر 100 کاراکتر باشد")
    private String producer;

    @NotNull
    private LocalDateTime expireAt;

    @NotNull
    private DrugLocation location;

    @NotNull
    private DrugType drugType;

//    private List<HowToUse> howToUses;
//    private List<AmountOfUse> amountOfUses;
//    private List<UseTime> useTimes;

//    private String description;

    @NotNull
    @Min(value = 0, message = "حداقل مقدار ممکن برای موجودی 0 می باشد")
    @Max(value = 1000000, message = "حداکثر مقدار ممکن برای موجودی 1000000 می باشد")
    private Integer available;

    @NotNull
    @Min(value = 0, message = "حداقل مقدار ممکن برای موجودی پک 0 می باشد")
    @Max(value = 1000000, message = "حداکثر مقدار ممکن برای موجودی پک 1000000 می باشد")
    private Integer availablePack;

    @Min(value = 0, message = "حداقل مقدار ممکن برای قیمت 0 می باشد")
    private Integer price;

    @NotNull
    @Size(min = 1, max = 100, message = "شماره قفسه باید حداقل 1 و حداکثر 100 کاراکتر باشد")
    private String shelfNo;

    @NotNull
    @Size(min = 1, max = 100, message = "شماره جعبه باید حداقل 1 و حداکثر 100 کاراکتر باشد")
    private String boxNo;

    @NotNull
    @Size(min = 1, max = 100, message = "دوز باید حداقل 1 و حداکثر 100 کاراکتر باشد")
    private String dose;
//    @NotNull(message = "لطفا وضعیت نمایش دارو را وارد نمایید")
//    private Boolean visibility;

//    @Min(value = 1, message = "حداقل مقدار ممکن برای اولیت 1 می باشد")
//    @Max(value = 1000, message = "حداکثر مقدار ممکن برای اولویت 1000 می باشد")
//    @NotNull(message = "لطفا اولویت نمایش دارو را وارد نمایید")
//    private Integer priority;

}
