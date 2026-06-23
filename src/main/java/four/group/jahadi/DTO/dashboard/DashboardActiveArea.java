package four.group.jahadi.DTO.dashboard;

import four.group.jahadi.DTO.profile.ProfileDigest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class DashboardActiveArea {
    private List<ProfileDigest> groups;
    private ProfileDigest areaOwner;
    private String name;
    private String city;
}
