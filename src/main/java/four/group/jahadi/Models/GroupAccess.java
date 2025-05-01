package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

import static four.group.jahadi.Utility.Utility.printNullableField;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupAccess {

    @Field("group_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    ObjectId groupId;

    @Field("write_access")
    Boolean writeAccess;

    @Transient
    private Group group;

    @Override
    public String toString() {
        return "{" +
                "\"groupId\":" + printNullableField(groupId) +
                ", \"writeAccess\":" + writeAccess +
                '}';
    }
}
