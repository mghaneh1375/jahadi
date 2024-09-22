package four.group.jahadi.DTO;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class WareHouseAccessForGroupData {
    ObjectId userId;
    boolean drugAccess;
    boolean equipmentAccess;
}
