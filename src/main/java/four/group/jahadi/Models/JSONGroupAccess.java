package four.group.jahadi.Models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JSONGroupAccess {

    @JsonSerialize(using = ObjectIdSerialization.class)
    ObjectId groupId;

    Boolean writeAccess;
}
