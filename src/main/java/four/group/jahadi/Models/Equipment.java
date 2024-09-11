package four.group.jahadi.Models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.EquipmentHealthStatus;
import four.group.jahadi.Enums.EquipmentType;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "equipment")
@Builder
public class Equipment extends Model {
    @Field("equipment_type")
    private EquipmentType equipmentType;
    private String name;
    private String producer;
    private Integer available;
    @Field("buy_at")
    @JsonSerialize(using = DateSerialization.class)
    private Date buyAt;
    @Field("used_at")
    @JsonSerialize(using = DateSerialization.class)
    private Date usedAt;
    @Field("guarantee_expire_at")
    @JsonSerialize(using = DateSerialization.class)
    private Date guaranteeExpireAt;
    @Field("health_status")
    private EquipmentHealthStatus healthStatus;
    @Field("row_no")
    private String rowNo;
    @Field("shelf_no")
    private String shelfNo;
    private String location;
    private String description;
    private ObjectId userId;
    private ObjectId groupId;
    @Field("property_id")
    private String propertyId;
}
