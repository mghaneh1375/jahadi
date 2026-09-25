package four.group.jahadi.Service.dashboard;

import four.group.jahadi.DTO.Area.AreaDigest;
import four.group.jahadi.DTO.Trip.TripDigest;
import four.group.jahadi.DTO.groupDashboard.PatientsAnswersDto;
import four.group.jahadi.DTO.groupDashboard.PerProvinceDto;
import four.group.jahadi.DTO.groupDashboard.TotalReceptedPatients;
import four.group.jahadi.DTO.groupDashboard.TripsPerMonthDto;
import four.group.jahadi.DTO.profile.ActiveTripsCartable;
import four.group.jahadi.DTO.profile.AreaStat;
import four.group.jahadi.DTO.profile.JahadgarCartable;
import four.group.jahadi.DTO.profile.ModuleStat;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.*;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.*;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Repository.Area.impl.*;
import four.group.jahadi.Repository.ModuleRepository;
import four.group.jahadi.Repository.ProjectRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Repository.UserRepository;
import four.group.jahadi.Utility.JalaliCalendar;
import four.group.jahadi.Utility.PairValue;
import four.group.jahadi.Utility.Utility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static four.group.jahadi.Service.dashboard.JahadgarDashboardService.IN_PROGRESS;
import static four.group.jahadi.Service.dashboard.JahadgarDashboardService.fetchAreaLaunchMode;

@Service
@RequiredArgsConstructor
public class GroupDashboardService {
    private final TripRepository tripRepository;
    private final PatientsInAreaRepository patientsInAreaRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ModuleRepository moduleRepository;
    private final CacheManager cacheManager;

    public ResponseEntity<JahadgarCartable> cartable(
            ObjectId userId
    ) {
        List<Trip> trips = tripRepository.findFullByGroupId(userId);
        List<AreaStat> allTrips = new ArrayList<>();
        HashMap<ObjectId, User> users = userRepository.findDigestByIdsIn(
                trips
                        .stream()
                        .map(Trip::getAreas)
                        .flatMap(Collection::stream)
                        .map(Area::getOwnerId)
                        .collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(
                User::getId,
                user -> user,
                (existing, replacement) -> replacement,
                HashMap::new
        ));

        for (Trip trip : trips) {
            for (Area area : trip.getAreas()) {
                User u = users.get(area.getOwnerId());
                AreaStat areaStat = AreaStat
                        .builder()
                        .id(area.getId())
                        .name(area.getName())
                        .tripName(trip.getName())
                        .tripId(trip.getId())
                        .role("مسئول گروه")
                        .city(area.getCity())
                        .start(area.getStartAt())
                        .end(area.getEndAt())
                        .moduleStats(
                                area.getModules() == null
                                        ? null
                                        : area.getModules()
                                        .stream()
                                        .filter(moduleInArea -> moduleInArea.getMembers() != null)
                                        .map(moduleInArea ->
                                                ModuleStat
                                                        .builder()
                                                        .id(moduleInArea.getModuleId())
                                                        .name(moduleInArea.getModuleName())
                                                        .build())
                                        .collect(Collectors.toList())
                        )
                        .ownerName(u.getName())
                        .status(fetchAreaLaunchMode(area.getStartAt(), area.getEndAt()))
                        .build();

                allTrips.add(areaStat);
            }
        }

        return ResponseEntity.ok().body(
                JahadgarCartable
                        .builder()
                        .trips(allTrips)
                        .build()
        );
    }

    public ResponseEntity<List<Project>> projectsNeededAction(ObjectId groupId) {
        List<Project> projects =
                projectRepository.findActivesByOwner(Collections.singletonList(groupId), Utility.getCurrLocalDateTime());
        List<Project> result = new ArrayList<>();

        projects.forEach(project -> {
            List<Trip> trips =
                    tripRepository.findNeedActionByGroupId(Utility.getCurrLocalDateTime(), groupId, project.getId());

            if (trips.isEmpty())
                return;

            List<TripDigest> tripDigests = new ArrayList<>();
            for (Trip trip : trips) {
                if (trip.getGroupsWithAccess().stream().noneMatch(groupAccess ->
                        groupAccess.getWriteAccess() && groupAccess.getGroupId().equals(groupId)
                ))
                    continue;

                tripDigests.add(
                        TripDigest
                                .builder()
                                .id(trip.getId())
                                .name(trip.getName())
                                .startAt(trip.getStartAt())
                                .endAt(trip.getEndAt())
                                .dailyStartAt(trip.getDailyStartAt())
                                .dailyEndAt(trip.getDailyEndAt())
                                .areas(
                                        trip.getAreas().stream().map(area -> {
                                            return AreaDigest
                                                    .builder()
                                                    .id(area.getId())
                                                    .ownerId(area.getOwnerId())
                                                    .name(area.getName())
                                                    .build();
                                        }).collect(Collectors.toList())
                                )
                                .build()
                );
            }

            if (tripDigests.isEmpty())
                return;

            project.setTripDigests(tripDigests);
            result.add(project);
        });

        return ResponseEntity.ok().body(result);
    }

    public ResponseEntity<ActiveTripsCartable> activeTripsReport(
            ObjectId userId
    ) {
        List<Trip> trips = tripRepository.findActiveTripFullByGroupId(userId, LocalDateTime.now());
        List<AreaStat> activeTrips = new ArrayList<>();
        Set<ObjectId> userIds = new HashSet<>();

        for (Trip trip : trips) {
            for (Area area : trip.getAreas()) {
                String areaLaunchMode = fetchAreaLaunchMode(area.getStartAt(), area.getEndAt());
                if(!areaLaunchMode.equals(IN_PROGRESS))
                    continue;

                userIds.addAll(area.getMembers());
                AreaStat areaStat = AreaStat
                        .builder()
                        .id(area.getId())
                        .name(area.getName())
                        .role("مسئول اردو")
                        .tripName(trip.getName())
                        .city(area.getCity())
                        .start(area.getStartAt())
                        .end(area.getEndAt())
                        .isOwner(true)
                        .moduleStats(
                                area.getModules() == null
                                        ? null
                                        : area.getModules()
                                        .stream()
                                        .filter(moduleInArea -> moduleInArea.getMembers() != null)
                                        .map(moduleInArea ->
                                                ModuleStat
                                                        .builder()
                                                        .id(moduleInArea.getModuleId())
                                                        .name(moduleInArea.getModuleName())
                                                        .build())
                                        .collect(Collectors.toList())
                        )
                        .status(fetchAreaLaunchMode(area.getStartAt(), area.getEndAt()))
                        .members(area.getMembers())
                        .build();

                activeTrips.add(areaStat);
            }
        }

        ActiveTripsCartable activeTripsCartable = null;
        if (!activeTrips.isEmpty()) {
            final List<User> members = userIds.isEmpty()
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

                areaStat.getModuleStats().forEach(moduleStat -> {
                    statistics
                            .stream()
                            .filter(patientStatistics -> Objects.equals(patientStatistics.get_id().getAreaId(), areaStat.getId()) && Objects.equals(patientStatistics.get_id().getModuleId(), moduleStat.getId()))
                            .findFirst()
                            .ifPresent(patientStatistics -> {
                                moduleStat.setPatients(patientStatistics.getTotal());
                                moduleStat.setReceptedPatients(patientStatistics.getAccepted());
                                moduleStat.setWaitingPatients(patientStatistics.getTotal() - patientStatistics.getAccepted());
                            });
                });
            });

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

            activeTripsCartable = ActiveTripsCartable
                    .builder()
                    .activeTrips(activeTrips)
                    .build();
        }

        return ResponseEntity.ok().body(activeTripsCartable);
    }

    public ResponseEntity<GroupStatistics> allTripsForceReload(
            ObjectId groupId
    ) {
        String key = groupId.toString();
        Cache cache = cacheManager.getCache("group-all-trips");
        if (cache != null) {
            cache.evict(key);
        }

        return allTrips(groupId);
    }

    @Cacheable(
            value = "group-all-trips", key = "#groupId"
    )
    public ResponseEntity<GroupStatistics> allTrips(ObjectId groupId) {
        GroupStatistics groupStatistics = tripRepository.getGroupStatistics(groupId);
        groupStatistics.setRefreshAt(LocalDateTime.now());

        return ResponseEntity.ok()
                .body(groupStatistics);
    }

    public ResponseEntity<PerProvinceDto> perProvinceTripForceReload(
            ObjectId groupId, String from, String to
    ) {
        String key = groupId + "_" + from + "_" + to;
        Cache cache = cacheManager.getCache("perProvinceTrip");
        if (cache != null) {
            cache.evict(key);
        }

        return perProvinceTrip(
                groupId, from, to
        );
    }

    @Cacheable(
            value = "perProvinceTrip", key = "#groupId + '_' + #from + '_' + #to"
    )
    public ResponseEntity<PerProvinceDto> perProvinceTrip(
            ObjectId groupId, String from, String to
    ) {
        return ResponseEntity.ok()
                .body(
                        PerProvinceDto
                                .builder()
                                .data(
                                        tripRepository.countTripsByStateInDateRange(
                                                groupId,
                                                Utility.getLocalDateTime(
                                                        Utility.convertJalaliToGregorianDate(from)
                                                ),
                                                Utility.getLocalDateTime(
                                                        Utility.convertJalaliToGregorianDate(to)
                                                )
                                        )
                                )
                                .refreshAt(LocalDateTime.now())
                                .build()
                );
    }


    public ResponseEntity<TotalReceptedPatients> getTotalReceptedPatientsForceReload(ObjectId groupId) {
        String key = groupId.toString();
        Cache cache = cacheManager.getCache("group-total-recepted-patients");
        if (cache != null) {
            cache.evict(key);
        }

        return getTotalReceptedPatients(groupId);
    }

    @Cacheable(
            value = "group-total-recepted-patients", key = "#groupId"
    )
    public ResponseEntity<TotalReceptedPatients> getTotalReceptedPatients(ObjectId groupId) {
        return ResponseEntity.ok().body(
                TotalReceptedPatients
                        .builder()
                        .data(tripRepository.countAcceptedPatientsByGroupId(groupId))
                        .refreshAt(LocalDateTime.now())
                        .build()
        );
    }

    public ResponseEntity<GroupMembersReport> membersReportForceReload(
            ObjectId groupId
    ) {
        String key = groupId.toString();
        Cache cache = cacheManager.getCache("group-members-report");
        if (cache != null) {
            cache.evict(key);
        }

        return membersReport(groupId);
    }

    @Cacheable(value = "group-members-report", key = "#groupId")
    public ResponseEntity<GroupMembersReport> membersReport(
            ObjectId groupId
    ) {
        GroupMembersReport groupMembersReport = new GroupMembersReport();
        Long membersInActiveAreas = tripRepository.countUniqueMembersInActiveAreas(groupId, LocalDateTime.now());
        groupMembersReport.setMembersInTrip(membersInActiveAreas == null ? 0L : membersInActiveAreas);
        List<GenderCount> members = userRepository.getGroupMemberCountByGender(groupId);
        int totalMembers = 0;
        for(GenderCount member : members) {
            if(member.get_id().equals(Sex.MALE.getName().toUpperCase()))
                groupMembersReport.setMenMembers(member.getCount());
            else
                groupMembersReport.setWomenMembers(member.getCount());

            totalMembers += member.getCount();
        }

        groupMembersReport.setGroupStatisticsResult(
                userRepository.getGroupStatistics(groupId)
        );
        groupMembersReport.setMembers(totalMembers);
        groupMembersReport.setRefreshAt(LocalDateTime.now());
        return ResponseEntity.ok()
                .body(groupMembersReport);
    }

    public ResponseEntity<TripsPerMonthDto> getTripsPerMonthForceReload(
            ObjectId groupId, String year
    ) {
        String key = groupId + "_" + year;
        Cache cache = cacheManager.getCache("group-trips-per-month");
        if (cache != null) {
            cache.evict(key);
        }

        return getTripsPerMonth(groupId, year);
    }

    @Cacheable(value = "group-trips-per-month", key = "#groupId + '_' + #year")
    public ResponseEntity<TripsPerMonthDto> getTripsPerMonth(ObjectId groupId, String year) {
        JalaliCalendar.YearMonthDate gregorian = JalaliCalendar.jalaliToGregorian(
                new JalaliCalendar.YearMonthDate(
                        Integer.parseInt(year), 1, 1
                )
        );
        List<MonthlyTripCount> existing = tripRepository.getMonthlyTripCountByGroupWithAllMonths(groupId, gregorian.getYear());

        Map<Integer, Long> monthMap = existing.stream()
                .filter(m -> m.getMonth() != null)
                .collect(Collectors.toMap(
                        MonthlyTripCount::getMonth,
                        MonthlyTripCount::getCount,
                        (a, b) -> a
                ));

        List<MonthlyTripCount> result = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            result.add(new MonthlyTripCount(month, monthMap.getOrDefault(month, 0L)));
        }

        return ResponseEntity.ok()
                .body(
                        TripsPerMonthDto
                                .builder()
                                .data(result)
                                .refreshAt(LocalDateTime.now())
                                .build()
                );
    }

    public ResponseEntity<PatientStats> getPatientsStatWithForceReload(
            ObjectId moduleId, ObjectId areaId,
            ObjectId tripId, ObjectId groupId
    ) {
        String key = moduleId + "_" + areaId + "_" + tripId + "_" + groupId;
        Cache cache = cacheManager.getCache("patientsStat");
        if (cache != null) {
            cache.evict(key);
        }

        return getPatientsStat(
                moduleId, areaId, tripId, groupId
        );
    }

    @Cacheable(value = "patientsStat", key = "#moduleId + '_' + #areaId + '_' + #tripId + '_' + #groupId")
    public ResponseEntity<PatientStats> getPatientsStat(
            ObjectId moduleId, ObjectId areaId,
            ObjectId tripId, ObjectId groupId
    ) {
        PatientStats patientStatistics =
                patientsInAreaRepository.getPatientStatistics(moduleId, areaId, tripId, groupId);
        patientStatistics.setRefreshAt(LocalDateTime.now());
        return ResponseEntity.ok().body(patientStatistics);
    }

    @Data
    @AllArgsConstructor
    static class OptionDigest {
        private PairValue option;
        private Integer counter;

        @Override
        public boolean equals(Object o) {
            if(o instanceof OptionDigest) {
                return Objects.equals(option.getKey(), ((OptionDigest)o).option.getKey());
            }
            return false;
        }

        public void inc() {
            this.counter++;
        }
    }

    @Data
    @AllArgsConstructor
    public static class QuestionDigest {
        private String text;
        private List<OptionDigest> optionsDigest;
    }

    public ResponseEntity<PatientsAnswersDto> getPatientsAnswersForceReload(
            ObjectId moduleId, ObjectId areaId,
            ObjectId tripId, ObjectId groupId
    ) {
        String key = moduleId + "_" + areaId + "_" + tripId + "_" + groupId;
        Cache cache = cacheManager.getCache("patientsAnswers");
        if (cache != null) {
            cache.evict(key);
        }

        return getPatientsAnswers(
                moduleId, areaId, tripId, groupId
        );
    }

    @Cacheable(value = "patientsAnswers", key = "#moduleId + '_' + #areaId + '_' + #tripId + '_' + #groupId")
    public ResponseEntity<PatientsAnswersDto> getPatientsAnswers(
            ObjectId moduleId, ObjectId areaId,
            ObjectId tripId, ObjectId groupId
    ) {
        Module module = moduleRepository.findById(moduleId).orElseThrow(InvalidIdException::new);
        HashMap<ObjectId, QuestionDigest> questions = new HashMap<>();
        for(SubModule subModule : module.getSubModules()) {
            fetchQuestionIds(subModule.getQuestions(), questions, null);
        }
        if(questions.isEmpty())
            return ResponseEntity.ok().build();

        List<GroupedAnswer> patientsAnswers = patientsInAreaRepository.getPatientsAnswers(
                moduleId, areaId, tripId, groupId, questions.keySet()
        );

        HashMap<String, OptionDigest> optionDigestHashMap = new HashMap();
        for(ObjectId objectId : questions.keySet()) {
            QuestionDigest questionDigest = questions.get(objectId);
            for(OptionDigest optionDigest : questionDigest.optionsDigest) {
                optionDigestHashMap.put(
                        objectId.toString() + "_" + optionDigest.option.getKey().toString(),
                        optionDigest
                );
            }
        }

        for (GroupedAnswer groupedAnswer : patientsAnswers) {
            for(String answer : groupedAnswer.getAnswers()) {
                optionDigestHashMap.get(groupedAnswer.getQuestionId().toString() + "_" + answer).inc();
            }
        }

        return ResponseEntity.ok().body(
                PatientsAnswersDto
                        .builder()
                        .data(new ArrayList<>(questions.values()))
                        .refreshTime(LocalDateTime.now())
                        .build()
        );
    }

    private void fetchQuestionIds(
            List<? extends Question> questions,
            HashMap<ObjectId, QuestionDigest> result,
            List<PairValue> options
    ) {
        for(Question question : questions) {
            if((question instanceof SimpleQuestion && options == null) ||
                    question instanceof TableQuestion ||
                    question instanceof GroupQuestion ||
                    question instanceof ListQuestion
            )
                continue;

            if(question instanceof SimpleQuestion) {
                result.put(
                        question.getId(),
                        new QuestionDigest(
                                ((SimpleQuestion) question).getQuestion(),
                                options.stream()
                                        .map(pairValue -> new OptionDigest(pairValue, 0))
                                        .collect(Collectors.toList())
                        )
                );
            }

            if(question instanceof CheckListGroupQuestion) {
                CheckListGroupQuestion checkListGroupQuestion = (CheckListGroupQuestion) question;
                fetchQuestionIds(
                        checkListGroupQuestion.getQuestions(),
                        result,
                        checkListGroupQuestion.getOptions()
                );
            }
        }
    }
}
