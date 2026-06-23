package four.group.jahadi.DTO.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.ObjectIdSerialization;
import four.group.jahadi.Models.PicSerialization;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Data
@SuperBuilder
public class ProfileDigest {
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = PicSerialization.class)
    private String pic;
}
