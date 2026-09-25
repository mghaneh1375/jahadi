package four.group.jahadi.DTO.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActiveCartable {
    private List<AreaStat> activeTrips;
    private Integer totalReceptedByMe;
}
