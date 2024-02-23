package four.group.jahadi.DTO.Area;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AreaDrugsData {

    @NotNull ObjectId drugId;

    @NotNull
    @Min(0)
    @Max(10000)
    Integer totalCount;

}
