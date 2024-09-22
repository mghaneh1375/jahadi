package four.group.jahadi.Models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
public class WareHouseAccessForGroupJoinWithUser extends WareHouseAccessForGroup {
    @JsonSerialize(using = UserDigestSerialization.class)
    private User user;
}
