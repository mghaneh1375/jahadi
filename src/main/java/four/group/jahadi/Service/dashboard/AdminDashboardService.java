package four.group.jahadi.Service.dashboard;

import four.group.jahadi.DTO.dashboard.*;
import four.group.jahadi.DTO.profile.ProfileDigest;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Models.*;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Repository.*;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Service.admin.ThresholdService;
import four.group.jahadi.Utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final TripRepository tripRepository;
    private final ProjectRepository projectRepository;
    private final DrugsInAreaRepository drugsInAreaRepository;
    private final PatientsInAreaRepository patientsInAreaRepository;
    private final ExternalReferralRepository externalReferralRepository;
    private final PatientRepository patientRepository;
    private final ThresholdService thresholdService;

    public AdminDashboardData get() {

        List<Trip> trips = tripRepository.findActivesOrNotStartedProjects2(Utility.getCurrLocalDateTime());
        List<Group> groups = groupRepository.findDigestByIdsIn(
                trips.stream().map(Trip::getGroupsWithAccess)
                        .map(groupAccesses -> groupAccesses.stream().map(GroupAccess::getGroupId).collect(Collectors.toList()))
                        .flatMap(List::stream).distinct().collect(Collectors.toList())
        );

        trips.forEach(trip -> trip.getGroupsWithAccess().forEach(groupAccess -> groups.stream()
                .filter(user -> user.getId().equals(groupAccess.getGroupId()))
                .findFirst().ifPresent(groupAccess::setGroup)));

        List<User> areaOwners = userRepository.findDigestByIdsIn(
                trips.stream().map(Trip::getAreas)
                        .map(areas -> areas.stream().map(Area::getOwnerId).collect(Collectors.toList()))
                        .flatMap(List::stream).distinct().collect(Collectors.toList())
        );
        trips.forEach(trip -> trip.getAreas().forEach(area -> areaOwners.stream()
                .filter(user -> user.getId().equals(area.getOwnerId()))
                .findFirst().ifPresent(area::setOwner)));

        List<DashboardActiveArea> dashboardActiveAreas = new ArrayList<>();
        trips.forEach(trip -> {
            dashboardActiveAreas.addAll(
                    trip.getAreas().stream().map(area -> DashboardActiveArea
                            .builder()
                            .groups(
                                    trip.getGroupsWithAccess()
                                    .stream()
                                    .map(GroupAccess::getGroup)
                                            .map(group ->
                                                    ProfileDigest
                                                            .builder()
                                                            .pic(group.getPic())
                                                            .name(group.getName())
                                                            .id(group.getId())
                                                            .build()
                                            ).collect(Collectors.toList())
                            )
                            .name(area.getName())
                            .city(area.getCity())
                            .areaOwner(
                                    ProfileDigest
                                            .builder()
                                            .id(area.getOwnerId())
                                            .name(area.getOwner().getName())
                                            .build()
                            )
                            .build()).collect(Collectors.toList())
            );
        });

        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        return AdminDashboardData
                .builder()
                .totalUsers(userRepository.countUsersByAccess(Access.JAHADI.getName().toUpperCase()))
                .totalGroups(groupRepository.totalCount())
                .totalProjects((int) projectRepository.count())
                .lastMonthTotalProjects(
                        projectRepository.getTotalProjectsCountInLastMonth(lastMonth)
                )
                .totalTrips((int) tripRepository.count())
                .lastMonthTotalTrips(
                        tripRepository.getTotalTripsCountInLastMonth(
                                lastMonth
                        )
                )
                .totalAreas(tripRepository.getTotalAreasCount())
                .lastMonthTotalAreas(tripRepository.getTotalAreasCountInLastMonth(
                        lastMonth
                ))
                .totalExternalServices(
                        (int) externalReferralRepository.count()
                )
                .lastMonthTotalExternalServices(
                        externalReferralRepository.getTotalExternalServicesCountInLastMonth(
                                lastMonth
                        )
                )
                .totalPatients(
                        (int) patientRepository.count()
                )
                .lastMonthTotalPatients(
                        patientsInAreaRepository.getTotalPatientsCountInLastMonth(
                                lastMonth
                        )
                )
                .currentAreas(
                        dashboardActiveAreas
                )
                .totalCurrentTrips(trips.size())
                .build();
    }

    @Cacheable(cacheNames = "areasPerProvince", key = "#from + '_' + #to")
    public PerProvinceStatDto areasPerProvince(
            String from, String to
    ) {
        return PerProvinceStatDto
                .builder()
                .report(
                        tripRepository.countTripsByStateInDateRange(
                                Utility.getLocalDateTime(
                                        Utility.convertJalaliToGregorianDate(from)
                                ),
                                Utility.getLastLocalDateTime(
                                        Utility.convertJalaliToGregorianDate(to)
                                )
                        )
                )
                .thresholds(
                        thresholdService.getThreshold(ReportType.AREAS_PER_PROVINCE)
                )
                .build();
    }

    @Cacheable(cacheNames = "membersPerProvince", key = "#from + '_' + #to")
    public PerProvinceStatDto membersPerProvince(
            String from, String to
    ) {
        return PerProvinceStatDto
                .builder()
                .report(
                        tripRepository.countMembersByStateInDateRange(
                                Utility.getLocalDateTime(
                                        Utility.convertJalaliToGregorianDate(from)
                                ),
                                Utility.getLastLocalDateTime(
                                        Utility.convertJalaliToGregorianDate(to)
                                )
                        )
                )
                .thresholds(
                        thresholdService.getThreshold(ReportType.MEMBERS_PER_PROVINCE)
                )
                .build();
    }

    @Cacheable(cacheNames = "patientsPerProvince", key = "#from + '_' + #to")
    public PerProvinceStatDto patientsPerProvince(
            String from, String to
    ) {
        return PerProvinceStatDto
                .builder()
                .report(
                        patientsInAreaRepository.countPatientsByStateInDateRange(
                                Utility.getLocalDateTime(
                                        Utility.convertJalaliToGregorianDate(from)
                                ),
                                Utility.getLastLocalDateTime(
                                        Utility.convertJalaliToGregorianDate(to)
                                )
                        )
                )
                .thresholds(
                        thresholdService.getThreshold(ReportType.PATIENTS_PER_PROVINCE)
                )
                .build();
    }

    @Cacheable(cacheNames = "drugsPerProvince", key = "#from + '_' + #to")
    public PerProvinceStatDto drugsPerProvince(
            String from, String to
    ) {
        return PerProvinceStatDto
                .builder()
                .report(
                        drugsInAreaRepository.countDrugsByStateInDateRange(
                                Utility.getLocalDateTime(
                                        Utility.convertJalaliToGregorianDate(from)
                                ),
                                Utility.getLastLocalDateTime(
                                        Utility.convertJalaliToGregorianDate(to)
                                )
                        )
                )
                .thresholds(
                        thresholdService.getThreshold(ReportType.DRUGS_PER_PROVINCE)
                )
                .build();
    }

    @Cacheable(cacheNames = "externalServicesPerProvince", key = "#from + '_' + #to")
    public PerProvinceStatDto externalServicesPerProvince(
            String from, String to
    ) {
        return PerProvinceStatDto
                .builder()
                .report(
                        externalReferralRepository.countExternalServicesByStateInDateRange(
                                Utility.getLocalDateTime(
                                        Utility.convertJalaliToGregorianDate(from)
                                ),
                                Utility.getLastLocalDateTime(
                                        Utility.convertJalaliToGregorianDate(to)
                                )
                        )
                )
                .thresholds(
                        thresholdService.getThreshold(ReportType.EXTERNAL_SERVICES_PER_PROVINCE)
                )
                .build();
    }

    @Cacheable(cacheNames = "finantialExternalServicesPerProvince", key = "#from + '_' + #to")
    public FinantialPerProvinceStatDto finantialExternalServicesPerProvince(
            String from, String to
    ) {
        return FinantialPerProvinceStatDto
                .builder()
                .report(
                        externalReferralRepository.amountSumExternalServicesByStateInDateRange(
                                Utility.getLocalDateTime(
                                        Utility.convertJalaliToGregorianDate(from)
                                ),
                                Utility.getLastLocalDateTime(
                                        Utility.convertJalaliToGregorianDate(to)
                                )
                        )
                )
                .thresholds(
                        thresholdService.getThreshold(ReportType.FINANCIAL_EXTERNAL_SERVICES_PER_PROVINCE)
                )
                .build();
    }
}
