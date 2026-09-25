package four.group.jahadi.Models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
public class ReportAccessJoinWithUser extends ReportAccessForGroup {
    @JsonSerialize(using = UserDigestSerialization.class)
    private User user;
}
