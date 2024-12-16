package four.group.jahadi.Models;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@Builder
public class TokenInfo {
    private ObjectId userId;
    private ObjectId groupId;
    private String username;
    private Collection<? extends GrantedAuthority> accesses;
}
