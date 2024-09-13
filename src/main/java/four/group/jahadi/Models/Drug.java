package four.group.jahadi.Models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.Drug.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "drug")
public class Drug extends Model {
    @Field("user_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId userId;
    @Field("group_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId groupId;
    @Field("drug_type")
    private DrugType drugType;
    @Field("expire_at")
    private Date expireAt;
    private String dose;
    private String name;
    private String producer;
    private Integer price;
    private String description;
    private Integer available;
    @Field("available_pack")
    private Integer availablePack;
    private List<ObjectId> replacements;
    private Boolean visibility;
    private Integer priority;
    private DrugLocation location;
    @Field("box_no")
    private String boxNo;
    @Field("shelf_no")
    private String shelfNo;
}
