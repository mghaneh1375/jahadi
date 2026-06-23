package four.group.jahadi.DTO.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AdminDashboardData {
    private Integer totalUsers;
    private Integer totalGroups;
    private Integer totalTrips;
    private Integer totalProjects;
    private Long totalAreas;
    private Integer totalPatients;
    private Integer totalExternalServices;

    private Long lastMonthTotalTrips;
    private Long lastMonthTotalProjects;
    private Long lastMonthTotalAreas;
    private Long lastMonthTotalPatients;
    private Long lastMonthTotalExternalServices;

    private Integer totalCurrentTrips;
    private Long totalExternalCost;
    private Long lastMonthTotalExternalCost;

    private List<DashboardActiveArea> currentAreas;
}
