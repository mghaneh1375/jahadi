package four.group.jahadi.DTO;

import four.group.jahadi.Enums.Drug.AmountOfUse;
import four.group.jahadi.Enums.Drug.HowToUse;
import four.group.jahadi.Enums.Drug.UseTime;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class DrugBookmarkData {
    private HowToUse howToUses;
    private AmountOfUse amountOfUses;
    private UseTime useTimes;
}
