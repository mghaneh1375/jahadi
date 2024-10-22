package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer available;
    @Field("buy_at")
    @JsonSerialize(using = DateSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date buyAt;
    @Field("used_at")
    @JsonSerialize(using = DateSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date usedAt;
    @Field("guarantee_expire_at")
    @JsonSerialize(using = DateSerialization.class)
    private Date guaranteeExpireAt;
    @Field("health_status")
    private EquipmentHealthStatus healthStatus;
    @Field("row_no")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rowNo;
    @Field("shelf_no")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String shelfNo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String location;
    private String description;
    @Field("user_id")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId userId;
    @Field("group_id")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId groupId;
    @Field("property_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String propertyId;
}
