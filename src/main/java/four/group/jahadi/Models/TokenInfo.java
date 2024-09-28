package four.group.jahadi.Models;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@Builder
public class TokenInfo {
    private ObjectId userId;
    private ObjectId groupId;
    private String username;
}
