package four.group.jahadi.Service.Area;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import four.group.jahadi.DTO.Area.AreaDigest;
import four.group.jahadi.DTO.UpdatePresenceList;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Area.AreaDates;
import four.group.jahadi.Models.Area.PatientsInArea;
import four.group.jahadi.Models.Country;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Models.User;
import four.group.jahadi.Models.UserPresenceList;
import four.group.jahadi.Repository.Area.AreaDrugsRepository;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Repository.Area.PresenceListRepository;
import four.group.jahadi.Repository.*;
import four.group.jahadi.Service.*;
import four.group.jahadi.Utility.Utility;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static four.group.jahadi.Service.Area.AreaUtils.findArea;
import static four.group.jahadi.Utility.Utility.snakeToCamel;

@Service
public class AreaService {
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
    private NoteRepository noteRepository;
    @Autowired
    private AreaPresenceService areaPresenceService;
    @Autowired
    private WareHouseAccessForGroupRepository wareHouseAccessForGroupRepository;
    @Autowired
    private DrugServiceInArea drugServiceInArea;
    @Autowired
    private EquipmentServiceInArea equipmentServiceInArea;
    @Autowired
    private PresenceListRepository presenceListRepository;
    @Autowired
    private DrugRepository drugRepository;
    @Autowired
    private AreaDrugsRepository areaDrugsRepository;
    @Autowired
    private IOService ioService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ExportUtils exportUtils;

    @Value("${app.package-scan.model}")
    private String modelPackages;

    @Value("${app.package-scan.repository}")
    private String repositoryPackages;

    public ResponseEntity<List<Trip>> myCartableList(ObjectId userId, boolean isForOwner) {

        List<Trip> trips = isForOwner
                ? tripRepository.findNotFinishedByAreaOwnerId(Utility.getCurrLocalDateTime(), userId)
                : tripRepository.findNotFinishedByMemberId(Utility.getCurrLocalDateTime(), userId);

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
        if (!wareHouseAccessForGroupRepository.existsAccessByGroupIdAndUserId(groupId, userId))
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
        if (!wareHouseAccessForGroupRepository.existsAccessByGroupIdAndUserId(groupId, userId))
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
        output.put("startAt", Utility.convertUTCDateToJalali(foundArea.getStartAt()));
        output.put("endAt", Utility.convertUTCDateToJalali(foundArea.getEndAt()));
        output.put("lat", foundArea.getLat());
        output.put("lng", foundArea.getLng());

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    public List<UserPresenceList> getPresenceList(ObjectId userId, ObjectId areaId) {
        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId).orElseThrow(InvalidIdException::new);
        Area foundArea = findArea(trip, areaId, userId);
        return areaPresenceService.getAreaPresenceList(areaId, userRepository.findByIdsIn(foundArea.getMembers()));
    }

    private void validateSubmitEntrance(ObjectId userId, ObjectId areaId, ObjectId jahadgarId) {

        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId).orElseThrow(InvalidIdException::new);
        Area foundArea = findArea(trip, areaId, userId);

        LocalDateTime now = Utility.getCurrLocalDateTime();

        if (foundArea.getStartAt() == null || foundArea.getStartAt().isAfter(now))
            throw new InvalidFieldsException("اردو در منطقه موردنظر هنوز شروع نشده است");

        if (foundArea.getEndAt().isBefore(now))
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
                add(AreaDates.builder().start(LocalDateTime.now()).build());
            }});
        else if (foundArea.getDates().get(foundArea.getDates().size() - 1).getEnd() == null)
            throw new RuntimeException("اردو پیش از این شروع شده است");
        else
            foundArea.getDates().add(AreaDates.builder().start(LocalDateTime.now()).build());

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
            foundArea.getDates().get(foundArea.getDates().size() - 1).setEnd(LocalDateTime.now());

        tripRepository.save(trip);
    }

    public ResponseEntity<List<AreaDates>> getRegionTimesHistory(ObjectId userId, ObjectId areaId) {

        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId).orElseThrow(InvalidIdException::new);
        Area foundArea = findArea(trip, areaId, userId);

        if (foundArea.getDates() == null)
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);

        return new ResponseEntity<>(foundArea.getDates(), HttpStatus.OK);
    }

    public void exportArea(
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

            List<ObjectId> neededUsersId = new ArrayList<>();
            neededUsersId.addAll(foundArea.getMembers());
            neededUsersId.add(foundArea.getOwnerId());

            ioService.export(noteRepository.findByUsersIdIn(neededUsersId), outputStream, "Note");

            exportUtils.exportAreaTrip(trip, areaId, outputStream);
            exportUtils.exportAreaDrugs(foundArea, outputStream);
            exportUtils.exportEquipments(foundArea, outputStream);
            exportUtils.exportPatients(outputStream);

            response.setStatus(HttpStatus.OK.value());
            response.flushBuffer();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public static Set<Class<?>> getClassesWithAnnotation(
            Class<? extends Annotation> annotation,
            String... packageNames
    ) {
        Set<Class<?>> annotatedClasses = new HashSet<>();
        for (int i = 0; i < packageNames.length; i++) {
            try {
                ScanResult scanResult = new ClassGraph()
                        .enableAllInfo()
                        .acceptPackages(packageNames[i])
                        .scan();
                annotatedClasses.addAll(
                        scanResult.getClassesWithAnnotation(annotation.getName())
                                .loadClasses()
                );
            }
            catch (Exception ignore) {}
        }

//        try {
//            Set<Class<?>> allClasses = findAllClassesUsingClassLoader(packageNames);
//            for (Class<?> clazz : allClasses) {
//                if (clazz.isAnnotationPresent(annotation)) {
//                    annotatedClasses.add(clazz);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
        return annotatedClasses;
    }

    public static Set<Class<?>> findAllClassesUsingClassLoader(String... packageNames) {
        Set<Class<?>> output = new HashSet<>();
//        System.out.println(packageNames.length);
        for (int i = 0; i < packageNames.length; i++) {
            String packageName = packageNames[i];
//            System.out.println(packageName);
            InputStream stream = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream(packageName.replaceAll("[.]", "/"));
            if(stream == null) {
                System.out.println("Stream is null");
                continue;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            output.addAll(
                    reader.lines()
                            .filter(line -> line.endsWith(".class"))
                            .map(line -> getClass(line, packageName))
                            .collect(Collectors.toSet())
            );
//            System.out.println("Output len is " + output.size());
        }

        return output;
    }

    private static Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }

    private void saveAllList(
            List<Object> values, Class<?> selectedDB,
            Set<Class<?>> repositories
    ) {
//        System.out.println("Saving data for " + selectedDB.getAnnotation(Document.class).collection());
        String className = snakeToCamel(selectedDB.getAnnotation(Document.class).collection());

        repositories
                .stream()
                .filter(aClass -> aClass.getAnnotation(MyRepository.class).model().equalsIgnoreCase(className))
                .findFirst().ifPresent(aClass -> {
//                    System.out.println("Repository found: " + aClass.getName());
                    BeanFetcher fetcher = new BeanFetcher(applicationContext);
                    Object bean = fetcher.getBeanByClass(aClass);
                    try {
                        Method method = aClass.getMethod("saveAll", Iterable.class);
                        method.invoke(bean, values);
                    } catch (NoSuchMethodException | InvocationTargetException |
                             IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void removeAll(
            Class<?> selectedDB, Set<Class<?>> repositories
    ) {
        String className = snakeToCamel(selectedDB.getAnnotation(Document.class).collection());
        repositories
                .stream()
                .filter(aClass -> aClass.getAnnotation(MyRepository.class).model().equalsIgnoreCase(className))
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
        Set<Class<?>> models = getClassesWithAnnotation(Document.class,
                modelPackages.split(",")
        );
        Set<Class<?>> repositories = getClassesWithAnnotation(MyRepository.class,
                repositoryPackages.split(",")
        );
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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
//                        System.out.println("New Table: " + line);
                        if (values != null && values.size() > 0) {
                            saveAllList(values, selectedDB.get(), repositories);
                        }
                        String wanted = line.replaceAll("\\*", "");
                        models
                                .stream()
                                .filter(aClass ->
                                        snakeToCamel(aClass.getAnnotation(Document.class).collection()).equalsIgnoreCase(wanted)
                                )
                                .findFirst().ifPresent(aClass -> {
//                                    System.out.println("Find model: " + aClass.getAnnotation(Document.class).collection());
                                    selectedDB.set(aClass);
                                    removeAll(selectedDB.get(), repositories);
                                });

                        values = new ArrayList<>();
                        continue;
                    }
                    if (selectedDB.get() == null)
                        continue;
//                    System.out.println("line is " + line);
//                    System.out.println(objectMapper.readValue(line, selectedDB.get()));
                    values.add(objectMapper.readValue(line, selectedDB.get()));
                } catch (Exception x) {
                    x.printStackTrace();
                }
            }

            if (selectedDB.get() != null && values != null && values.size() > 0) {
                try {
                    saveAllList(values, selectedDB.get(), repositories);
                } catch (Exception x) {
                    x.printStackTrace();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
