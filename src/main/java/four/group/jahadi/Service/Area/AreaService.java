package four.group.jahadi.Service.Area;

import four.group.jahadi.DTO.Area.AreaData;
import four.group.jahadi.DTO.Region.RegionRunInfoData;
import four.group.jahadi.DTO.Region.RegionSendNotifData;
import four.group.jahadi.DTO.UpdatePresenceList;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.*;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Area.AreaDates;
import four.group.jahadi.Repository.*;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Service.AbstractService;
import four.group.jahadi.Service.AreaPresenceService;
import four.group.jahadi.Service.NotifService;
import four.group.jahadi.Utility.Utility;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

        ObjectId tripId = (ObjectId) params[1];

        Trip trip = tripRepository.findById(tripId).orElseThrow(InvalidIdException::new);

        boolean hasAdminAccess = (boolean) params[0];

        if (!hasAdminAccess && trip.getGroupsWithAccess().stream().noneMatch(groupAccess ->
                groupAccess.getWriteAccess() && groupAccess.getGroupId().equals(params[2]))
        )
            throw new NotAccessException();

        List<Area> areaModels = new ArrayList<>();

        for (AreaData dto : areas) {

            Area area = dto.convertToArea();

            User owner = userRepository.findById(area.getOwnerId()).orElseThrow(InvalidIdException::new);

            if (!hasAdminAccess && !Objects.deepEquals(owner.getGroupId(), params[2]))
                throw new NotAccessException();

            if (trip.getAreas().stream().anyMatch(area1 -> area1.getName().equals(area.getName())))
                throw new InvalidFieldsException("منطقه ای با نام مشابه موجود است");

            areaModels.add(area);
        }

        trip.getAreas().addAll(areaModels);
        tripRepository.save(trip);

        return new ResponseEntity<>(areaModels, HttpStatus.OK);
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

        Date start = getDate(getDate(new Date(dto.getStartAt())));
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
}
