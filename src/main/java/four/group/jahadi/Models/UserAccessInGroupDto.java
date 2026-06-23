package four.group.jahadi.Models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccessInGroupDto {
    @JsonSerialize(using = UserDigestSerialization.class)
    private User user;
    private Boolean drugAccess;
    private Boolean equipmentAccess;
    private Boolean externalReferralAccess;
}
