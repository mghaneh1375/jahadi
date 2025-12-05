package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.Drug.DrugLocation;
import four.group.jahadi.Enums.Drug.DrugType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static four.group.jahadi.Utility.Utility.*;

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
    @JsonSerialize(using = JustDateSerialization.class)
    private LocalDateTime expireAt;
    private String dose;
    private String name;
    private String code;
    private String producer;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer price;
    private String description;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer available;
    @Field("available_pack")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer availablePack;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ObjectId> replacements;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean visibility;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer priority;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private DrugLocation location;
    @Field("box_no")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String boxNo;
    @Field("shelf_no")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String shelfNo;

    @Field("deleted_at")
    @JsonIgnore
    private Date deletedAt;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(this.getId()) +
                ", \"createdAt\":" + printNullableDate(this.getCreatedAt()) +
                ", \"userId\":" + printNullableField(userId) +
                ", \"groupId\":" + printNullableField(groupId) +
                ", \"drugType\":" + printNullableField(drugType) +
                ", \"expireAt\":" + printNullableDate(expireAt) +
                ", \"dose\":" + printNullableField(dose) +
                ", \"name\":" + printNullableField(name) +
                ", \"producer\":" + printNullableField(producer) +
                ", \"price\":" + printNullableInteger(price) +
                ", \"description\":" + printNullableField(description) +
                ", \"available\":" + printNullableInteger(available) +
                ", \"availablePack\":" + printNullableInteger(availablePack) +
                ", \"replacements\":" + toStringOfList(replacements) +
                ", \"visibility\":" + visibility +
                ", \"priority\":" + printNullableInteger(priority) +
                ", \"location\":" + printNullableField(location) +
                ", \"boxNo\":" + printNullableField(boxNo) +
                ", \"shelfNo\":" + printNullableField(shelfNo) +
                "}\n";
    }
}
