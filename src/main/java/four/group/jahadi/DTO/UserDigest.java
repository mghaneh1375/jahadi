package four.group.jahadi.DTO;

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
public class UserDigest {
    private String name;
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId id;
}
