package four.group.jahadi.DTO.Digest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyAccesses {
    private boolean drugAccess;
    private boolean equipmentAccess;
    private boolean externalReferralAccess;
    private boolean reportAccess;
}
