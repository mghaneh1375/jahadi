package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupAccess {

    @Field("group_id")
    @JsonIgnore
    ObjectId groupId;

    @Field("write_access")
    Boolean writeAccess;

    @Transient
    private Group group;
}
