package four.group.jahadi.DTO;

import four.group.jahadi.Enums.Drug.AmountOfUse;
import four.group.jahadi.Enums.Drug.HowToUse;
import four.group.jahadi.Enums.Drug.UseTime;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Validated
public class DrugBookmarkData {
    private HowToUse howToUses;
    private AmountOfUse amountOfUses;
    private UseTime useTimes;
    @Min(value = 1, message = "تعداد نباید کمتر از 1 باشد")
    @Max(value = 1000000, message = "تعداد نباید بزرگ تر از 1000000 باشد")
    private Integer amount;
}
