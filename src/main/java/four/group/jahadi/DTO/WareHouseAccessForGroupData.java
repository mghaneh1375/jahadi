package four.group.jahadi.DTO;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class WareHouseAccessForGroupData {
    @NonNull
    ObjectId userId;
    Boolean drugAccess;
    Boolean equipmentAccess;
}
