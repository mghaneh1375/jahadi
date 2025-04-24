package four.group.jahadi.Service.Area;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import four.group.jahadi.DTO.Area.AreaData;
import four.group.jahadi.DTO.Area.AreaDigest;
import four.group.jahadi.DTO.Area.UpdateAreaData;
import four.group.jahadi.DTO.Region.RegionRunInfoData;
import four.group.jahadi.DTO.Region.RegionSendNotifData;
import four.group.jahadi.DTO.UpdatePresenceList;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Area.AreaDates;
import four.group.jahadi.Models.Area.PatientsInArea;
import four.group.jahadi.Models.*;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Repository.Area.PresenceListRepository;
import four.group.jahadi.Repository.*;
import four.group.jahadi.Service.*;
import four.group.jahadi.Utility.Utility;
import four.group.jahadi.Utility.ValidList;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static four.group.jahadi.Service.Area.AreaUtils.findArea;
import static four.group.jahadi.Utility.Utility.getDate;
import static four.group.jahadi.Utility.Utility.getLastDate;

@Service
public class AreaService extends AbstractService<Area, AreaData> {

    @Autowired
    private NotifService notifService;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PatientsInAreaRepository patientsInAreaRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private AreaPresenceService areaPresenceService;
    @Autowired
    private WareHouseAccessForGroupRepository wareHouseAccessForGroupRepository;
    @Autowired
    private ExternalReferralAccessForGroupRepository externalReferralAccessForGroupRepository;
    @Autowired
    private DrugServiceInArea drugServiceInArea;
    @Autowired
    private EquipmentServiceInArea equipmentServiceInArea;
    @Autowired
    private PresenceListRepository presenceListRepository;
    @Autowired
    private DrugRepository drugRepository;
    @Autowired
    private IOService ioService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ExportUtils exportUtils;

    @Override
    public ResponseEntity<List<Area>> list(Object... filters) {
        return null;
    }

    @Override
    public void update(ObjectId id, AreaData dto, Object... params) {

    }

    @Override
    public ResponseEntity<Area> store(AreaData data, Object... params) {
        return null;
    }

    public ResponseEntity<List<Area>> store(List<AreaData> areas, Object... params) {

        boolean hasAdminAccess = (boolean) params[0];
        ObjectId tripId = (ObjectId) params[1];
        Object groupId = params[2];

        Trip trip = tripRepository.findById(tripId).orElseThrow(InvalidIdException::new);

        if (!hasAdminAccess && trip.getGroupsWithAccess().stream().noneMatch(groupAccess ->
                groupAccess.getWriteAccess() && groupAccess.getGroupId().equals(groupId))
        )
            throw new NotAccessException();

        List<Area> areaModels = new ArrayList<>();
        for (AreaData dto : areas) {
            Area area = dto.convertToArea();
            User owner = userRepository.findById(area.getOwnerId()).orElseThrow(InvalidIdException::new);

            if (!hasAdminAccess && !Objects.deepEquals(owner.getGroupId(), groupId))
                throw new NotAccessException();

            if (trip.getAreas().stream().anyMatch(area1 -> area1.getName().equals(area.getName())))
                throw new InvalidFieldsException("منطقه ای با نام مشابه موجود است");

            areaModels.add(area);
        }

        trip.getAreas().addAll(areaModels);
        tripRepository.save(trip);

        return new ResponseEntity<>(areaModels, HttpStatus.OK);
    }

    public void updateAreas(
            ObjectId tripId, ValidList<UpdateAreaData> areas,
            boolean hasAdminAccess, ObjectId userId, ObjectId groupId
    ) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(InvalidIdException::new);
        if (!hasAdminAccess && trip.getGroupsWithAccess().stream().noneMatch(groupAccess ->
                groupAccess.getWriteAccess() && groupAccess.getGroupId().equals(groupId))
        )
            throw new NotAccessException();

        areas.forEach(updateAreaData -> {
            trip
                    .getAreas()
                    .stream()
                    .filter(area -> area.getId().equals(updateAreaData.getAreaId()))
                    .findFirst().orElseThrow(InvalidIdException::new);
        });

        areas.forEach(updateAreaData -> {
            Area area1 = trip
                    .getAreas()
                    .stream()
                    .filter(area -> area.getId().equals(updateAreaData.getAreaId()))
                    .findFirst().orElseThrow(InvalidIdException::new);

            area1.setOwnerId(updateAreaData.getOwner());
            area1.setName(updateAreaData.getName());
            area1.setColor(updateAreaData.getColor());
        });

        tripRepository.save(trip);
    }

    @Override
    public ResponseEntity<Area> findById(ObjectId id, Object... params) {
        return null;
    }

    public void sendTripAlarmToAllMembers(ObjectId userId, ObjectId areaId,
                                          @RequestBody @Valid RegionSendNotifData dto) {

        if (!dto.getSendNotif() && !dto.getSendSMS())
            return;

        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = trip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        if (!foundArea.getFinished())
            throw new InvalidFieldsException("اطلاعات منطقه هنوز نهایی نشده است");

        if (dto.getSendNotif())
            notifService.sendTripNotifToAllMembers(foundArea, dto.getMsg());

    }

    public ResponseEntity<List<Trip>> myCartableList(ObjectId userId, boolean isForOwner) {

        List<Trip> trips = isForOwner
                ? tripRepository.findNotFinishedByAreaOwnerId(Utility.getCurrDate(), userId)
                : tripRepository.findNotFinishedByMemberId(Utility.getCurrDate(), userId);

        if (trips != null && trips.size() > 0) {
            trips.forEach(trip -> {
                if (trip.getAreas() != null && trip.getAreas().size() > 0) {
                    if (isForOwner) {
                        trip.getAreas().removeIf(area ->
                                area.getOwnerId() == null ||
                                        !area.getOwnerId().equals(userId)
                        );
                    } else {
                        trip.getAreas().removeIf(area ->
                                area.getMembers() == null ||
                                        !area.getMembers().contains(userId)
                        );
                    }
                }
            });
            if (!isForOwner) {
                Set<ObjectId> ownerIds = new HashSet<>();
                trips
                        .stream()
                        .filter(trip -> trip.getAreas() != null && trip.getAreas().size() > 0)
                        .forEach(trip -> trip.getAreas().forEach(area -> ownerIds.add(area.getOwnerId())));
                List<User> owners = userRepository.findDigestByIdsIn(new ArrayList<>(ownerIds));
                trips
                        .stream()
                        .filter(trip -> trip.getAreas() != null && trip.getAreas().size() > 0)
                        .forEach(trip -> trip.getAreas().forEach(area -> {
                            area.setOwner(
                                    owners.stream().filter(user -> user.getId().equals(area.getOwnerId()))
                                            .findFirst().get()
                            );
                            area.setOwnerId(null);
                            area.setMembers(null);
                        }));
            }
        }

        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    public ResponseEntity<List<AreaDigest>> getGroupAreas(
            ObjectId tripId, ObjectId userId, ObjectId groupId
    ) {
        if (!wareHouseAccessForGroupRepository.existsAccessByGroupIdAndUserId(groupId, userId) &&
                !externalReferralAccessForGroupRepository.existsAccessByGroupIdAndUserId(groupId, userId)
        )
            throw new NotAccessException();

        List<Trip> trips = tripId == null
                ? tripRepository.findDigestInfoProjectsByGroupId(groupId)
                : tripRepository.findDigestInfoProjectsByGroupIdAndTripId(groupId, tripId);

        List<AreaDigest> areaDigests = new ArrayList<>();
        trips.forEach(trip -> {
            trip.getAreas().forEach(area -> {
                areaDigests.add(
                        AreaDigest
                                .builder()
                                .id(area.getId())
                                .name(area.getName())
                                .build()
                );
            });
        });
        return new ResponseEntity<>(areaDigests, HttpStatus.OK);
    }

    public ResponseEntity<List<AreaDigest>> getGroupTrips(ObjectId userId, ObjectId groupId) {
        if (!wareHouseAccessForGroupRepository.existsAccessByGroupIdAndUserId(groupId, userId) &&
                !externalReferralAccessForGroupRepository.existsAccessByGroupIdAndUserId(groupId, userId)
        )
            throw new NotAccessException();

        List<Trip> trips = tripRepository.findDigestTripInfoProjectsByGroupId(groupId);
        List<AreaDigest> digests = new ArrayList<>();
        trips.forEach(trip -> {
            digests.add(
                    AreaDigest
                            .builder()
                            .id(trip.getId())
                            .name(trip.getName())
                            .build()
            );
        });
        return new ResponseEntity<>(digests, HttpStatus.OK);
    }

    public ResponseEntity<HashMap<String, Boolean>> staticAccesses(ObjectId userId, ObjectId areaId) {
        Trip wantedTrip = tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area area = findArea(wantedTrip, areaId);
        HashMap<String, Boolean> accesses = new HashMap<>();
        accesses.put("pharmacy", area.getOwnerId().equals(userId) || (area.getPharmacyManagers() != null && area.getPharmacyManagers().contains(userId)));
        accesses.put("dispatcher", area.getOwnerId().equals(userId) || (area.getDispatchers() != null && area.getDispatchers().contains(userId)));
        accesses.put("equipment", area.getOwnerId().equals(userId) || (area.getEquipmentManagers() != null && area.getEquipmentManagers().contains(userId)));
        accesses.put("laboratory", area.getOwnerId().equals(userId) || (area.getLaboratoryManager() != null && area.getLaboratoryManager().contains(userId)));
        accesses.put("insurance", area.getOwnerId().equals(userId) || (area.getInsurancers() != null && area.getInsurancers().contains(userId)));
        accesses.put("trainer", area.getOwnerId().equals(userId) || (area.getTrainers() != null && area.getTrainers().contains(userId)));

        return new ResponseEntity<>(accesses, HttpStatus.OK);
    }

    public ResponseEntity<HashMap<String, Object>> dashboard(ObjectId userId, ObjectId areaId) {

        Trip wantedTrip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area area = findArea(wantedTrip, areaId, userId);

        List<User> members = userRepository.findByIdsIn(area.getMembers());
        AtomicInteger maleMembers = new AtomicInteger();
        AtomicInteger femaleMembers = new AtomicInteger();

        members.forEach(user -> {
            if (user.getSex().equals(Sex.MALE))
                maleMembers.getAndIncrement();
            else
                femaleMembers.getAndIncrement();
        });

        HashMap<String, Object> data = new HashMap<>();
        data.put("maleMembers", maleMembers);
        data.put("femaleMembers", femaleMembers);
        data.put("finalized", area.getFinished());
        data.put("hasTrainSection", area.getTrainers() != null && area.getTrainers().size() > 0);
        data.put("hasInsuranceSection", area.getInsurancers() != null && area.getInsurancers().size() > 0);
//        data.put("hasLaboratorySection", area.getLaboratoryManager() != null && area.getLaboratoryManager().size() > 0);
//        data.put("hasPharmacySection", area.getPharmacyManagers() != null && area.getPharmacyManagers().size() > 0);
        data.put("hasLaboratorySection", true);
        data.put("hasPharmacySection", true);
        data.put("patients", patientsInAreaRepository.countByAreaId(areaId));

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    public void setRunInfo(ObjectId userId, ObjectId areaId, RegionRunInfoData dto) {

        City city = cityRepository.findById(new ObjectId(dto.getCityId())).orElseThrow(() -> {
            throw new InvalidFieldsException("آی دی شهر وارد شده نامعتبر است");
        });

        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Date start = getDate(new Date(dto.getStartAt()));
        Date end = getLastDate(new Date(dto.getEndAt()));

        if (trip.getStartAt().after(start))
            throw new InvalidFieldsException("زمان آغاز باید بعد از " + Utility.convertDateToJalali(trip.getStartAt()) + " باشد");

        if (trip.getEndAt().before(end))
            throw new InvalidFieldsException("زمان پایان باید قبل از " + Utility.convertDateToJalali(trip.getEndAt()) + " باشد");

        Area foundArea = trip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        State state = stateRepository.findById(city.getStateId()).orElseThrow(RuntimeException::new);
        Country country = countryRepository.findById(state.getCountryId()).orElseThrow(RuntimeException::new);

        foundArea.setCity(city.getName());
        foundArea.setState(state.getName());
        foundArea.setCityId(city.getId());
        foundArea.setStateId(state.getId());
        foundArea.setCountry(country.getName());
        foundArea.setDailyStartAt(dto.getDailyStartAt());
        foundArea.setDailyEndAt(dto.getDailyEndAt());
        foundArea.setStartAt(start);
        foundArea.setEndAt(end);
        foundArea.setLat(dto.getLat());
        foundArea.setLng(dto.getLng());

        tripRepository.save(trip);
    }

    public ResponseEntity<HashMap<String, Object>> getRunInfo(ObjectId userId, ObjectId areaId) {

        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = trip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        HashMap<String, Object> output = new HashMap<>();
        output.put("city", foundArea.getCity());
        output.put("state", foundArea.getState());
        output.put("cityId", foundArea.getCityId().toString());
        output.put("stateId", foundArea.getStateId().toString());
        Optional<Country> country = countryRepository.findByName(foundArea.getCountry());
        country.ifPresent(value -> output.put("countryId", value.getId().toString()));

        output.put("country", foundArea.getCountry());
        output.put("dailyStartAt", foundArea.getDailyStartAt());
        output.put("dailyEndAt", foundArea.getDailyEndAt());
        output.put("startAt", Utility.convertDateToJalali(foundArea.getStartAt()));
        output.put("endAt", Utility.convertDateToJalali(foundArea.getEndAt()));
        output.put("lat", foundArea.getLat());
        output.put("lng", foundArea.getLng());

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    public void finalizeArea(ObjectId userId, ObjectId areaId) {

        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId).orElseThrow(InvalidIdException::new);

        Area foundArea = trip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        if (foundArea.getFinished())
            return;

        if (foundArea.getCity() == null || foundArea.getLat() == null ||
                foundArea.getLng() == null || foundArea.getStartAt() == null ||
                foundArea.getEndAt() == null || foundArea.getDailyStartAt() == null ||
                foundArea.getDailyEndAt() == null
        )
            throw new InvalidFieldsException("لطفا اطلاعات مربوط به منطقه (مختصات جغرافیایی، روز و ساعت شروع و پایان، شهر محل برگزاری و ...) را وارد نمایید");

        if (foundArea.getMembers().size() == 0)
            throw new InvalidFieldsException("لطفا جهادگری به منطقه خود اضافه نمایید");

        if (foundArea.getModules().size() == 0)
            throw new InvalidFieldsException("لطفا بخشی به بخش های منطقه خود اضافه نمایید");

        if (foundArea.getDispatchers().size() == 0)
            throw new InvalidFieldsException("لطفا جهادگری را به عنوان فرد مسئول پذیرش انتخاب نمایید");

        foundArea.getModules().forEach(moduleInArea -> {
            if (moduleInArea.getMembers().size() == 0)
                throw new InvalidFieldsException("متولی بخش " + moduleInArea.getModuleName() + " را مشخص نکرده اید");
        });

        foundArea.setFinished(true);
        tripRepository.save(trip);
    }

    public List<UserPresenceList> getPresenceList(ObjectId userId, ObjectId areaId) {
        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId).orElseThrow(InvalidIdException::new);
        Area foundArea = findArea(trip, areaId, userId);
        return areaPresenceService.getAreaPresenceList(areaId, userRepository.findByIdsIn(foundArea.getMembers()));
    }

    private void validateSubmitEntrance(ObjectId userId, ObjectId areaId, ObjectId jahadgarId) {

        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId).orElseThrow(InvalidIdException::new);
        Area foundArea = findArea(trip, areaId, userId);

        Date now = Utility.getCurrDate();

        if (foundArea.getStartAt() == null || foundArea.getStartAt().after(now))
            throw new InvalidFieldsException("اردو در منطقه موردنظر هنوز شروع نشده است");

        if (foundArea.getEndAt().before(now))
            throw new InvalidFieldsException("اردو در منطفه موردنظر به اتمام رسیده است");

        if (foundArea.getMembers().stream().noneMatch(objectId -> objectId.equals(jahadgarId)))
            throw new InvalidFieldsException("جهادگر موردنظر در این منطقه وجود ندارد");
    }

    public void submitEntrance(ObjectId userId, ObjectId areaId, ObjectId jahadgarId) {
        validateSubmitEntrance(userId, areaId, jahadgarId);
        areaPresenceService.submitEntrance(areaId, jahadgarId);
    }

    public void updatePresenceList(
            ObjectId userId, ObjectId areaId,
            ObjectId jahadgarId, ObjectId presenceListId,
            UpdatePresenceList data
    ) {
        validateSubmitEntrance(userId, areaId, jahadgarId);
        areaPresenceService.updateEntrance(areaId, jahadgarId, presenceListId, data);
    }

    public void start(ObjectId userId, ObjectId areaId) {

        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId).orElseThrow(InvalidIdException::new);
        Area foundArea = findArea(trip, areaId, userId);

        if (foundArea.getDates() == null)
            foundArea.setDates(new ArrayList<>() {{
                add(AreaDates.builder().start(new Date()).build());
            }});
        else if (foundArea.getDates().get(foundArea.getDates().size() - 1).getEnd() == null)
            throw new RuntimeException("اردو پیش از این شروع شده است");
        else
            foundArea.getDates().add(AreaDates.builder().start(new Date()).build());

        tripRepository.save(trip);
    }

    public void end(ObjectId userId, ObjectId areaId) {

        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId).orElseThrow(InvalidIdException::new);
        Area foundArea = findArea(trip, areaId, userId);

        if (foundArea.getDates() == null)
            throw new RuntimeException("اردو هنوز شروع نشده است");
        else if (foundArea.getDates().get(foundArea.getDates().size() - 1).getEnd() != null)
            throw new RuntimeException("اردو پیش از این تمام شده است");
        else
            foundArea.getDates().get(foundArea.getDates().size() - 1).setEnd(new Date());

        tripRepository.save(trip);
    }

    public ResponseEntity<List<AreaDates>> getRegionTimesHistory(ObjectId userId, ObjectId areaId) {

        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId).orElseThrow(InvalidIdException::new);
        Area foundArea = findArea(trip, areaId, userId);

        if (foundArea.getDates() == null)
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);

        return new ResponseEntity<>(foundArea.getDates(), HttpStatus.OK);
    }

    public void remove(
            Trip trip, ObjectId areaId,
            ObjectId userId, String username, boolean needRemove
    ) {
        Optional<Area> first = trip
                .getAreas()
                .stream()
                .filter(area1 -> area1.getId().equals(areaId))
                .findFirst();
        if (first.isEmpty())
            throw new InvalidIdException();

        if (
                first.get().getStartAt() != null &&
                        Utility.getDate(first.get().getStartAt()).before(Utility.getCurrDate())
        )
            throw new RuntimeException("منطقه موردنظر شروع شده است و امکان حذف آن وجود ندارد");

        patientsInAreaRepository.deleteByAreaId(areaId);
        areaPresenceService.removeByAreaId(areaId);
        drugServiceInArea.returnAllDrugsByAdmin(
                userId, username, areaId,
                first.get().getName(), trip.getName()
        );
        equipmentServiceInArea.returnAllEquipmentsByAdmin(
                userId, username, areaId,
                first.get().getName(), trip.getName()
        );
        if (needRemove)
            trip.getAreas().removeIf(area -> area.getId().equals(areaId));
    }

    public void removeAreaFromTrip(
            ObjectId tripId, ObjectId areaId,
            ObjectId groupId, ObjectId userId, String username
    ) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(InvalidIdException::new);
        if (trip.getGroupsWithAccess()
                .stream()
                .noneMatch(groupAccess -> groupAccess.getWriteAccess() && groupAccess.getGroupId().equals(groupId))
        )
            throw new NotAccessException();

        remove(trip, areaId, userId, username, true);
        tripRepository.save(trip);
    }

    public void exportTrip(
            ObjectId areaId, ObjectId userId,
            HttpServletResponse response
    ) {
        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId).orElseThrow(InvalidIdException::new);
        Area foundArea = findArea(trip, areaId, userId);

        response.setContentType("application/octet-stream");
        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.builder("attachment").filename("tmp.json").build().toString()
        );
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            response.setStatus(HttpStatus.OK.value());
            response.flushBuffer();

            List<PatientsInArea> patientsInArea = patientsInAreaRepository.findByAreaId(areaId);
            ioService.export(patientsInArea, outputStream, "PatientsInArea");
            response.setStatus(HttpStatus.OK.value());
            response.flushBuffer();
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public void exportAllForConfigLocalServer(
            ObjectId areaId, ObjectId userId,
            HttpServletResponse response
    ) {
        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId).orElseThrow(InvalidIdException::new);
        Area foundArea = findArea(trip, areaId, userId);

        try {
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/octet-stream");
            response.setHeader(
                    HttpHeaders.CONTENT_DISPOSITION,
                    ContentDisposition.builder("attachment").filename("export.json").build().toString()
            );

            // no need for export activation table
            exportUtils.exportCommon(outputStream);
            exportUtils.exportUsers(foundArea, outputStream);
            exportUtils.exportTrip(trip, areaId, outputStream);
            exportUtils.exportGroups(trip, outputStream);
            exportUtils.exportDrugs(foundArea, outputStream);
            exportUtils.exportEquipments(foundArea, outputStream);
            exportUtils.exportPatients(outputStream);
            response.setStatus(HttpStatus.OK.value());
            response.flushBuffer();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public Set<Class> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    private Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }

    private void saveAllList(
            List<Object> values, Class selectedDB,
            Set<Class> repositories
    ) {
        System.out.println("Saving data for " + selectedDB.getName());
        List<Object> finalValues = values;
        String[] split = selectedDB.getName().split("\\.");
        repositories
                .stream()
                .filter(aClass -> aClass.getName().endsWith("." + split[split.length - 1] + "Repository"))
                .findFirst().ifPresent(aClass -> {
                    System.out.println("Repository found: " + aClass.getName());
                    BeanFetcher fetcher = new BeanFetcher(applicationContext);
                    Object bean = fetcher.getBeanByClass(aClass);
                    try {
                        Method method = aClass.getMethod("saveAll", Iterable.class);
                        method.invoke(bean, finalValues);
                    } catch (NoSuchMethodException | InvocationTargetException |
                             IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void removeAll(
            List<Object> values, Class selectedDB,
            Set<Class> repositories
    ) {
        List<Object> finalValues = values;
        String[] split = selectedDB.getName().split("\\.");
        repositories
                .stream()
                .filter(aClass -> aClass.getName().endsWith("." + split[split.length - 1] + "Repository"))
                .findFirst().ifPresent(aClass -> {
                    BeanFetcher fetcher = new BeanFetcher(applicationContext);
                    Object bean = fetcher.getBeanByClass(aClass);
                    try {
                        Method method = aClass.getMethod("deleteAll");
                        method.invoke(bean);
                    } catch (NoSuchMethodException | InvocationTargetException |
                             IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public void importDBToConstructLocalServer(MultipartFile file) {
        Set<Class> models = findAllClassesUsingClassLoader("four.group.jahadi.Models");
        models.addAll(findAllClassesUsingClassLoader("four.group.jahadi.Models.Area"));
        Set<Class> repositories = findAllClassesUsingClassLoader("four.group.jahadi.Repository");
        repositories.addAll(findAllClassesUsingClassLoader("four.group.jahadi.Repository.Area"));
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            InputStream inputStream = file.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            AtomicReference<Class> selectedDB = new AtomicReference<>(null);
            List<Object> values = null;
            while (reader.ready()) {
                try {
                    String line = reader.readLine();
                    if (line.length() < 5)
                        continue;
                    if (selectedDB.get() == null && !line.matches("^\\*\\*\\*\\*\\*\\*\\*[a-zA-Z]*\\*\\*\\*\\*\\*\\*\\*$"))
                        continue;
                    if (line.matches("^\\*\\*\\*\\*\\*\\*\\*[a-zA-Z]*\\*\\*\\*\\*\\*\\*\\*$")) {
                        System.out.println("New Table: " + line);
                        if (values != null && values.size() > 0) {
                            saveAllList(values, selectedDB.get(), repositories);
                        }
                        List<Object> finalValues = values;
                        models
                                .stream()
                                .filter(aClass -> aClass.getName().endsWith("." + line.replaceAll("\\*", "")))
                                .findFirst().ifPresent(aClass -> {
                                    System.out.println("Find model: " + aClass.getName());
                                    selectedDB.set(aClass);
                                    removeAll(finalValues, selectedDB.get(), repositories);
                                });

                        values = new ArrayList<>();
                        continue;
                    }
                    if (selectedDB.get() == null)
                        continue;

                    values.add(objectMapper.readValue(line, selectedDB.get()));
                }
                catch (Exception x) {
                    x.printStackTrace();
                }
            }

            if (selectedDB.get() != null && values != null && values.size() > 0) {
                try {
                    saveAllList(values, selectedDB.get(), repositories);
                }
                catch (Exception x) {
                    x.printStackTrace();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
