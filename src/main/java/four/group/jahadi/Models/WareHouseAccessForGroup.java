package four.group.jahadi.Models;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ware_house_access_for_group")
@Builder
public class WareHouseAccessForGroup extends Model {

    @Field("group_id")
    private ObjectId groupId;
    @Field("user_id")
    private ObjectId userId;
    @Field("has_access_for_drug")
    private Boolean hasAccessForDrug;
    @Field("has_access_for_equipment")
    private Boolean hasAccessForEquipment;
}
