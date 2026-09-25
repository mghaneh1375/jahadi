package four.group.jahadi.Routes.API.GroupAPIRoutes;


import four.group.jahadi.DTO.groupDashboard.PatientsAnswersDto;
import four.group.jahadi.DTO.groupDashboard.PerProvinceDto;
import four.group.jahadi.DTO.groupDashboard.TotalReceptedPatients;
import four.group.jahadi.DTO.groupDashboard.TripsPerMonthDto;
import four.group.jahadi.DTO.profile.ActiveTripsCartable;
import four.group.jahadi.DTO.profile.JahadgarCartable;
import four.group.jahadi.Models.Project;
import four.group.jahadi.Repository.Area.impl.GroupMembersReport;
import four.group.jahadi.Repository.Area.impl.GroupStatistics;
import four.group.jahadi.Repository.Area.impl.PatientStats;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.dashboard.GroupDashboardService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/api/group/dashboard")
@Validated
@RequiredArgsConstructor
public class GroupDashboardAPIRoutes extends Router {

    private final GroupDashboardService groupDashboardService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<JahadgarCartable> dashboard(
            HttpServletRequest request
    ) {
        return groupDashboardService.cartable(getGroup(request));
    }

    @GetMapping(value = "projects-needed-action")
    @ResponseBody
    public ResponseEntity<List<Project>> projectsNeededAction(
            HttpServletRequest request
    ) {
        return groupDashboardService.projectsNeededAction(getGroup(request));
    }

    @GetMapping(value = "active-trips")
    @ResponseBody
    public ResponseEntity<ActiveTripsCartable> activeTrips(
            HttpServletRequest request
    ) {
        return groupDashboardService.activeTripsReport(getGroup(request));
    }

    @GetMapping(value = "all-trips")
    @ResponseBody
    public ResponseEntity<GroupStatistics> allTrips(
            HttpServletRequest request,
            @RequestParam(required = false, value = "forceRefresh") Boolean forceRefresh
    ) {
        if(Objects.equals(Boolean.TRUE, forceRefresh)) {
            return groupDashboardService.allTripsForceReload(getGroup(request));
        }
        return groupDashboardService.allTrips(getGroup(request));
    }

    @GetMapping(value = "per-province-trips")
    @ResponseBody
    public ResponseEntity<PerProvinceDto> perProvinceTrip(
            HttpServletRequest request,
            @RequestParam(value = "from") String from,
            @RequestParam(value = "to") String to,
            @RequestParam(value = "forceReload", required = false) Boolean forceReload
    ) {
        if(Objects.equals(Boolean.TRUE, forceReload)) {
            return groupDashboardService.perProvinceTripForceReload(getGroup(request), from, to);
        }

        return groupDashboardService.perProvinceTrip(getGroup(request), from, to);
    }

    @GetMapping(value = "get-total-recepted-patients")
    @ResponseBody
    public ResponseEntity<TotalReceptedPatients> getTotalReceptedPatients(
            HttpServletRequest request,
            @RequestParam(required = false, value = "forceReload") Boolean forceReload
    ) {
        if(Objects.equals(Boolean.TRUE, forceReload)) {
            return groupDashboardService.getTotalReceptedPatientsForceReload(getGroup(request));
        }

        return groupDashboardService.getTotalReceptedPatients(getGroup(request));
    }

    @GetMapping(value = "members-report")
    @ResponseBody
    public ResponseEntity<GroupMembersReport> membersReport(
            HttpServletRequest request,
            @RequestParam(required = false, value = "forceReload") Boolean forceReload
    ) {
        if(Objects.equals(Boolean.TRUE, forceReload)) {
            return groupDashboardService.membersReportForceReload(getGroup(request));
        }

        return groupDashboardService.membersReport(getGroup(request));
    }

    @GetMapping(value = "get-trips-per-month")
    @ResponseBody
    public ResponseEntity<TripsPerMonthDto> getTripsPerMonth(
            HttpServletRequest request,
            @RequestParam(value = "year") @NotNull @Pattern(regexp = "^(13|14)\\d{2}$") String year,
            @RequestParam(value = "forceReload", required = false) Boolean forceReload
    ) {
        if(Objects.equals(Boolean.TRUE, forceReload)) {
            return groupDashboardService.getTripsPerMonthForceReload(getGroup(request), year);
        }

        return groupDashboardService.getTripsPerMonth(getGroup(request), year);
    }

    @GetMapping(value = "get-patients-stat")
    @ResponseBody
    public ResponseEntity<PatientStats> getPatientsStat(
            HttpServletRequest request,
            @RequestParam(required = false, value = "tripId") ObjectId tripId,
            @RequestParam(required = false, value = "areaId") ObjectId areaId,
            @RequestParam(required = false, value = "moduleId") ObjectId moduleId,
            @RequestParam(required = false, value = "forceReload") Boolean forceReload
    ) {
        if(Objects.equals(Boolean.TRUE, forceReload)) {
            return groupDashboardService.getPatientsStatWithForceReload(
                    moduleId, areaId, tripId, getGroup(request)
            );
        }

        return groupDashboardService.getPatientsStat(moduleId, areaId, tripId, getGroup(request));
    }

    @GetMapping(value = "get-patients-answers")
    @ResponseBody
    public ResponseEntity<PatientsAnswersDto> getPatientsAnswers(
            HttpServletRequest request,
            @RequestParam(required = false, value = "tripId") ObjectId tripId,
            @RequestParam(required = false, value = "areaId") ObjectId areaId,
            @RequestParam(value = "moduleId") @ObjectIdConstraint ObjectId moduleId,
            @RequestParam(required = false, value = "forceReload") Boolean forceReload
    ) {
        if(Objects.equals(Boolean.TRUE, forceReload)) {
            return groupDashboardService.getPatientsAnswersForceReload(
                    moduleId, areaId, tripId, getGroup(request)
            );
        }

        return groupDashboardService.getPatientsAnswers(moduleId, areaId, tripId, getGroup(request));
    }
}
