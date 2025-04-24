package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.Color;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

import static four.group.jahadi.Utility.Utility.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("project")
@Builder
public class Project extends Model {

    private String name;

    @JsonSerialize(using = ColorSerialization.class)
    private Color color;
    private int progress = 0;

    @Field("start_at")
    @JsonSerialize(using = DateSerialization.class)
    private Date startAt;

    @Field("end_at")
    @JsonSerialize(using = DateSerialization.class)
    private Date endAt;

    @Field("group_ids")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ObjectId> groupIds;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private List<String> groupNames;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private List<Group> groups;

    @Transient
    @JsonSerialize(using = ObjectIdListSerialization.class)
    private List<ObjectId> tripIds;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(this.getId()) +
                ", \"createdAt\":" + printNullableDate(this.getCreatedAt()) +
                ", \"name\":" + printNullableField(name) +
                ", \"color\":" + printNullableField(color) +
                ", \"progress\":" + printNullableInteger(progress) +
                ", \"startAt\":" + printNullableDate(startAt) +
                ", \"endAt\":" + printNullableDate(endAt) +
                ", \"groupIds\":" + toStringOfList(groupIds) +
                "}\n";
    }
}
