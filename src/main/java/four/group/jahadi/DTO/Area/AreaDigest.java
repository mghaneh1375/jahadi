package four.group.jahadi.DTO.Area;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AreaDigest {
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId ownerId;
}
