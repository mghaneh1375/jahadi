package four.group.jahadi.DTO;

import four.group.jahadi.Validator.ValidatedDrugLogFilter;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ValidatedDrugLogFilter
public class DrugLogFilter {

    private ObjectId drugId;
    private Long startAt;
    private Long endAt;
    private Boolean justPositives;
    private Boolean justNegatives;

}
