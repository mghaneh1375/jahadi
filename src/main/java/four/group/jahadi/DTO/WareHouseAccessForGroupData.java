package four.group.jahadi.DTO;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class WareHouseAccessForGroupData {
    @NonNull
    ObjectId userId;
    Boolean drugAccess;
    Boolean equipmentAccess;
}
