package four.group.jahadi.Service.Area;

import four.group.jahadi.DTO.AreaData;
import four.group.jahadi.DTO.Region.RegionRunInfoData;
import four.group.jahadi.DTO.Region.RegionSendNotifData;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.*;
import four.group.jahadi.Repository.*;
import four.group.jahadi.Service.AbstractService;
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

import static four.group.jahadi.Service.Area.MembersServiceInArea.fetchMemberIds;

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


    @Override
    public ResponseEntity<List<Area>> list(Object... filters) {
        return null;
    }

    @Override
    public void update(ObjectId id, AreaData dto, Object... params) {

    }

    @Override
    public ResponseEntity<Area> store(AreaData dto, Object... params) {

        ObjectId tripId = (ObjectId) params[1];

        Trip trip = tripRepository.findById(tripId).orElseThrow(InvalidIdException::new);

        boolean hasAdminAccess = (boolean) params[0];

        if (!hasAdminAccess && trip.getGroupsWithAccess().stream().noneMatch(groupAccess ->
                groupAccess.getWriteAccess() && groupAccess.getGroupId().equals(params[2]))
        )
            throw new NotAccessException();

        Area area = dto.convertToArea();

        User owner = userRepository.findById(area.getOwnerId()).orElseThrow(InvalidIdException::new);

        if (!hasAdminAccess && !Objects.deepEquals(owner.getGroupId(), params[2]))
            throw new NotAccessException();

        if (trip.getAreas().stream().anyMatch(area1 -> area1.getName().equals(area.getName())))
            throw new InvalidFieldsException("منطقه ای با نام مشابه موجود است");

        trip.getAreas().add(area);
        tripRepository.save(trip);

        return new ResponseEntity<>(area, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Area> findById(ObjectId id, Object... params) {
        return null;
    }

    public void sendTripAlarmToAllMembers(ObjectId userId, ObjectId areaId,
                                          @RequestBody @Valid RegionSendNotifData dto) {

        if(!dto.getSendNotif() && !dto.getSendSMS())
            return;

        Trip trip = tripRepository.findNotStartedByAreaOwnerId(new Date(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = trip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        if(!foundArea.isFinished())
            throw new InvalidFieldsException("اطلاعات منطقه هنوز نهایی نشده است");

        if(dto.getSendNotif())
            notifService.sendTripNotifToAllMembers(foundArea, dto.getMsg());

    }

    public ResponseEntity<List<Trip>> myCartableList(ObjectId userId) {
        return new ResponseEntity<>(
                tripRepository.findNotFinishedByAreaOwnerId(new Date(), userId),
                HttpStatus.OK
        );
    }

    public ResponseEntity<HashMap<String, Object>> dashboard(ObjectId userId, ObjectId areaId) {

        List<User> members = userRepository.findBy_idIn(fetchMemberIds(tripRepository, userId, areaId));
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
        data.put("patients", patientsInAreaRepository.countByAreaId(areaId));

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    public void setRunInfo(ObjectId userId, ObjectId areaId, RegionRunInfoData dto) {

        City city = cityRepository.findById(new ObjectId(dto.getCityId())).orElseThrow(() -> {
            throw new InvalidFieldsException("آی دی شهر وارد شده نامعتبر است");
        });

        Trip trip = tripRepository.findNotStartedByAreaOwnerId(new Date(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        Date start = new Date(dto.getStartAt());
        Date end = new Date(dto.getEndAt());

        if(trip.getStartAt().after(start))
            throw new InvalidFieldsException("زمان آغاز باید بعد از " + Utility.convertDateToJalali(trip.getStartAt()) + " باشد");

        if(trip.getEndAt().before(end))
            throw new InvalidFieldsException("زمان پایان باید قبل از " + Utility.convertDateToJalali(trip.getEndAt()) + " باشد");

        Area foundArea = trip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        State state = stateRepository.findById(city.getStateId()).orElseThrow(RuntimeException::new);
        Country country = countryRepository.findById(state.getCountryId()).orElseThrow(RuntimeException::new);

        foundArea.setCity(city.getName());
        foundArea.setState(state.getName());
        foundArea.setCountry(country.getName());
        foundArea.setDailyStartAt(dto.getDailyStartAt());
        foundArea.setDailyEndAt(dto.getDailyEndAt());
        foundArea.setStartAt(start);
        foundArea.setEndAt(end);
        foundArea.setLat(dto.getLat());
        foundArea.setLng(dto.getLng());

        tripRepository.save(trip);
    }

    public void finalizeArea(ObjectId userId, ObjectId areaId) {

        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId).orElseThrow(InvalidIdException::new);

        Area foundArea = trip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        if(foundArea.isFinished())
            return;

        if(foundArea.getCity() == null || foundArea.getLat() == null ||
                foundArea.getLng() == null || foundArea.getStartAt() == null ||
                foundArea.getEndAt() == null || foundArea.getDailyStartAt() == null ||
                foundArea.getDailyEndAt() == null
        )
            throw new InvalidFieldsException("لطفا اطلاعات مربوط به منطقه (مختصات جغرافیایی، روز و ساعت شروع و پایان، شهر محل برگزاری و ...) را وارد نمایید");

        if(foundArea.getMembers().size() == 0)
            throw new InvalidFieldsException("لطفا جهادگری به منطقه خود اضافه نمایید");

        if(foundArea.getModules().size() == 0)
            throw new InvalidFieldsException("لطفا بخشی به بخش های منطقه خود اضافه نمایید");

        if(foundArea.getDispatchers().size() == 0)
            throw new InvalidFieldsException("لطفا جهادگری را به عنوان فرد نوبت ده انتخاب نمایید");

        foundArea.getModules().forEach(moduleInArea -> {
            if(moduleInArea.getMembers().size() == 0)
                throw new InvalidFieldsException("متولی بخش " + moduleInArea.getModuleName() + " را مشخص نکرده اید");
        });

        foundArea.setFinished(true);
        tripRepository.save(trip);
    }
}
