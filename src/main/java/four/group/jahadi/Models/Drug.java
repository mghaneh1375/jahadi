package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private ObjectId userId;
    @Field("group_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private ObjectId groupId;
    @Field("drug_type")
    private DrugType drugType;
    @Field("expire_at")
    private Date expireAt;
    private String dose;
    private String name;
    private String producer;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer price;
    private String description;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer available;
    @Field("available_pack")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer availablePack;
    @JsonIgnore
    private List<ObjectId> replacements;
    @JsonIgnore
    private Boolean visibility;
    @JsonIgnore
    private Integer priority;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private DrugLocation location;
    @Field("box_no")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String boxNo;
    @Field("shelf_no")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String shelfNo;
}
