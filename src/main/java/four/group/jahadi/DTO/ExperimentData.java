package four.group.jahadi.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ExperimentData {

    @NotNull
    @Size(min = 2, max = 100, message = "عنوان باید حداقل 2 و حداکثر 100 کاراکتر باشد")
    private String title;

    @NotNull(message = "لطفا وضعیت نمایش را وارد نمایید")
    private Boolean visibility;

    @Min(value = 1, message = "حداقل مقدار ممکن برای اولیت 1 می باشد")
    @Max(value = 1000, message = "حداکثر مقدار ممکن برای اولویت 1000 می باشد")
    @NotNull(message = "لطفا اولویت نمایش آزمایش را وارد نمایید")
    private Integer priority;
}
