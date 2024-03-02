package four.group.jahadi.DTO;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import four.group.jahadi.Validator.ValidatedDrug;

@Getter
@Setter
@ValidatedDrug
public class DrugData {

    @NotNull
    @Size(min = 2, max = 100, message = "نام باید حداقل 2 و حداکثر 100 کاراکتر باشد")
    private String name;
    
    @Size(max = 1000, message = "طریقه مصرف باید حداکثر 1000 کاراکتر باشد")
    private String howToUse;
    
    private String description;

    @NotNull
    @Min(value = 0, message = "حداقل مقدار ممکن برای موجودی 0 می باشد")
    @Max(value = 1000000, message = "حداکثر مقدار ممکن برای موجودی 1000000 می باشد")
    private Integer available;
    
    @Min(value = 0, message = "حداقل مقدار ممکن برای قیمت 0 می باشد")
    private Integer price;

    @NotNull(message = "لطفا وضعیت نمایش دارو را وارد نمایید")
    private Boolean visibility;

    @Min(value = 1, message = "حداقل مقدار ممکن برای اولیت 1 می باشد")
    @Max(value = 1000, message = "حداکثر مقدار ممکن برای اولویت 1000 می باشد")
    @NotNull(message = "لطفا اولویت نمایش دارو را وارد نمایید")
    private Integer priority; 


}
