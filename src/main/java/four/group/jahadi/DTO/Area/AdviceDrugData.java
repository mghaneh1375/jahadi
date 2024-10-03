package four.group.jahadi.DTO.Area;

import four.group.jahadi.Enums.Drug.AmountOfUse;
import four.group.jahadi.Enums.Drug.HowToUse;
import four.group.jahadi.Enums.Drug.UseTime;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Validated
public class AdviceDrugData {
    @NotNull
    @Min(value = 1, message = "مقدار تجویز باید حداقل 1 باشد")
    @Max(value = 1000000, message = "مقدار تجویز باید حداکثر 1000000 باشد")
    Integer amount;

    @NotNull AmountOfUse amountOfUse;
    @NotNull HowToUse howToUse;
    @NotNull UseTime useTime;

    @Size(max = 1000, message = "متن توضیح باید حداکثر 1000 کاراکتر باشد")
    String description;
}
