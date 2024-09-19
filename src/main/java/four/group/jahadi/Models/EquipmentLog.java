package four.group.jahadi.Models;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "equipment_logs")
@Builder
public class EquipmentLog extends Model {

    @Field("equipment_id")
    private ObjectId equipmentId;
    @Field("user_id")
    private ObjectId userId;
    @Field("area_id")
    private ObjectId areaId;
    private Integer amount;
    private String desc;
}
