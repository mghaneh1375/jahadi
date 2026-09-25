package four.group.jahadi.Service.dashboard;

import four.group.jahadi.DTO.profile.ActiveCartable;
import four.group.jahadi.DTO.profile.AreaStat;
import four.group.jahadi.DTO.profile.JahadgarCartable;
import four.group.jahadi.DTO.profile.ModuleStat;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Area.ModuleInArea;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Repository.Area.impl.AreaPatientCount;
import four.group.jahadi.Repository.Area.impl.PatientStatistics;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JahadgarDashboardService {

    private final TripRepository tripRepository;
    private final PatientsInAreaRepository patientsInAreaRepository;
    private final UserRepository userRepository;

    public final static String IN_PROGRESS = "در حال برگزاری";
    public final static String FINISHED = "برگزار شده";
    public final static String NOT_START = "شروع نشده";

    public ResponseEntity<JahadgarCartable> cartable(
            ObjectId userId
    ) {
        List<Trip> trips = tripRepository.findByUserId(userId);
        List<AreaStat> activeTrips = new ArrayList<>();
        List<AreaStat> allTrips = new ArrayList<>();
        Set<ObjectId> userIds = new HashSet<>();

        for (Trip trip : trips) {
            for (Area area : trip.getAreas()) {
                if (!area.getOwnerId().equals(userId) &&
                        (
                                area.getMembers() == null ||
                                        !area.getMembers().contains(userId)
                        )
                )
                    continue;

                boolean isOwner = area.getOwnerId().equals(userId);
                if (isOwner) userIds.addAll(area.getMembers());

                AreaStat areaStat = AreaStat
                        .builder()
                        .id(area.getId())
                        .name(area.getName())
                        .role(fetchUserRoleInTrip(userId, area))
                        .city(area.getCity())
                        .start(area.getStartAt())
                        .end(area.getEndAt())
                        .isOwner(isOwner)
                        .moduleStats(
                                area.getModules() == null
                                        ? null
                                        : area.getModules()
                                        .stream()
                                        .filter(
                                                moduleInArea -> moduleInArea.getMembers() != null &&
                                                        (isOwner || moduleInArea.getMembers().contains(userId))
                                        )
                                        .map(moduleInArea ->
                                                ModuleStat
                                                        .builder()
                                                        .id(moduleInArea.getModuleId())
                                                        .name(moduleInArea.getModuleName())
                                                        .build())
                                        .collect(Collectors.toList())
                        )
                        .status(fetchAreaLaunchMode(area.getStartAt(), area.getEndAt()))
                        .build();

                if (isOwner) {
                    areaStat.setMembers(area.getMembers());
                    areaStat.setFinalized(area.getFinished());
                    areaStat.setStopReception(area.getStopReception());
                }
                allTrips.add(areaStat);
                if (areaStat.getStatus().equals(IN_PROGRESS)) {
                    activeTrips.add(areaStat);
                }
            }
        }

        ActiveCartable activeCartable = null;
        AtomicBoolean hasAnyOwnArea = new AtomicBoolean(false);

        if (activeTrips.size() > 0) {
            final List<User> members = userIds.size() == 0
                    ? null
                    : userRepository.findByIdsIn(new ArrayList<>(userIds));

            List<PatientStatistics> statistics = patientsInAreaRepository.getPatientStatisticsByAreasAndModules(
                    activeTrips
                            .stream()
                            .map(AreaStat::getId)
                            .collect(Collectors.toList()),
                    activeTrips
                            .stream()
                            .map(AreaStat::getModuleStats)
                            .filter(Objects::nonNull)
                            .flatMap(List::stream)
                            .map(ModuleStat::getId)
                            .distinct()
                            .collect(Collectors.toList()),
                    userId
            );

            activeTrips.forEach(areaStat -> {
                if (areaStat.getIsOwner()) {
                    hasAnyOwnArea.set(true);
                    AtomicInteger maleMembers = new AtomicInteger(0);
                    AtomicInteger femaleMembers = new AtomicInteger(0);

                    if (members != null) {
                        members
                                .stream()
                                .filter(user -> areaStat.getMembers() != null && areaStat.getMembers().contains(user.getId()))
                                .forEach(user -> {
                                    if (user.getSex().equals(Sex.MALE))
                                        maleMembers.getAndIncrement();
                                    else
                                        femaleMembers.getAndIncrement();
                                });
                    }

                    areaStat.setMaleMembers(maleMembers.get());
                    areaStat.setFemaleMembers(femaleMembers.get());
                }
                areaStat.getModuleStats().forEach(moduleStat -> {
                    statistics
                            .stream()
                            .filter(patientStatistics -> Objects.equals(patientStatistics.get_id().getAreaId(), areaStat.getId()) && Objects.equals(patientStatistics.get_id().getModuleId(), moduleStat.getId()))
                            .findFirst()
                            .ifPresent(patientStatistics -> {
                                moduleStat.setPatients(patientStatistics.getTotal());
                                moduleStat.setReceptedPatients(patientStatistics.getAccepted());
                                moduleStat.setReceptedByMe(patientStatistics.getAcceptedByDoctor());
                                moduleStat.setWaitingPatients(patientStatistics.getTotal() - patientStatistics.getAccepted());
                            });
                });
            });

            if (hasAnyOwnArea.get()) {
                List<ObjectId> objectIds = activeTrips
                        .stream()
                        .filter(AreaStat::getIsOwner)
                        .map(AreaStat::getId)
                        .collect(Collectors.toList());

                List<AreaPatientCount> totalPatientsByAreas = patientsInAreaRepository.getTotalPatientsByAreas(
                        objectIds
                );

                activeTrips.forEach(areaStat -> {
                    if (objectIds.contains(areaStat.getId()))
                        totalPatientsByAreas
                                .stream()
                                .filter(areaPatientCount -> areaPatientCount.get_id().equals(areaStat.getId()))
                                .findFirst()
                                .ifPresent(areaPatientCount -> {
                                    areaStat.setTotalPatients(
                                            areaPatientCount.getTotalPatients()
                                    );
                                });
                });
            }

            activeCartable = ActiveCartable
                    .builder()
                    .activeTrips(activeTrips)
                    .totalReceptedByMe(
                            patientsInAreaRepository.getTotalAcceptedPatientsByDoctor(
                                    userId
                            )
                    )
                    .build();
        }

        return ResponseEntity.ok().body(
                JahadgarCartable
                        .builder()
                        .activeCartable(activeCartable)
                        .trips(allTrips)
                        .build()
        );
    }

    public static String fetchAreaLaunchMode(LocalDateTime start, LocalDateTime end) {
        if(start == null || end == null)
            return NOT_START;
        LocalDateTime curr = LocalDateTime.now();
        if (start.isAfter(curr))
            return NOT_START;
        if (end.isBefore(curr))
            return FINISHED;

        return IN_PROGRESS;
    }

    public static String fetchUserRoleInTrip(ObjectId userId, Area area) {
        StringBuilder role = new StringBuilder();
        if (area.getOwnerId().equals(userId))
            role.append("مسئول منطقه").append(" - ");

        if (area.getInsurancers() != null &&
                area.getInsurancers().contains(userId)
        )
            role.append("مسئول بیمه").append(" - ");

        if (area.getDispatchers() != null &&
                area.getDispatchers().contains(userId)
        )
            role.append("مسئول پذیرش").append(" - ");

        if (area.getPharmacyManagers() != null &&
                area.getPharmacyManagers().contains(userId)
        )
            role.append("مسئول داروخانه").append(" - ");

        if (area.getTrainers() != null &&
                area.getTrainers().contains(userId)
        )
            role.append("مسئول آموزش").append(" - ");

        if (area.getEquipmentManagers() != null &&
                area.getEquipmentManagers().contains(userId)
        )
            role.append("مسئول تجهیز").append(" - ");

        if (area.getLaboratoryManager() != null &&
                area.getLaboratoryManager().contains(userId)
        )
            role.append("مسئول آزمایشگاه").append(" - ");

        if (area.getModules() != null) {
            for (ModuleInArea moduleInArea : area.getModules()) {
                if (moduleInArea.getMembers() != null &&
                        moduleInArea.getMembers().contains(userId)
                )
                    role.append(moduleInArea.getModuleName()).append(" - ");
            }
        }

        return role.length() == 0
                ? ""
                : role.substring(0, role.length() - 3);
    }
}
