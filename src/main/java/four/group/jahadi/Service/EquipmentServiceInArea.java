package four.group.jahadi.Service;

import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Area.AreaEquipments;
import four.group.jahadi.Models.Area.JoinedAreaEquipments;
import four.group.jahadi.Models.Equipment;
import four.group.jahadi.Models.EquipmentLog;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Repository.Area.AreaEquipmentsRepository;
import four.group.jahadi.Repository.Area.impl.EquipmentsInAreaRepositoryImp;
import four.group.jahadi.Repository.EquipmentLogRepository;
import four.group.jahadi.Repository.EquipmentRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Repository.WareHouseAccessForGroupRepository;
import four.group.jahadi.Service.Area.AreaUtils;
import four.group.jahadi.Utility.PairValue;
import four.group.jahadi.Utility.Utility;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceInArea {
    @Autowired
    private WareHouseAccessForGroupRepository wareHouseAccessForGroupRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private AreaEquipmentsRepository areaEquipmentsRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private EquipmentLogRepository equipmentLogRepository;
    @Autowired
    private EquipmentsInAreaRepositoryImp equipmentsInAreaRepositoryImp;

    private PairValue checkAccess(ObjectId userId, ObjectId areaId) {
        Trip trip = tripRepository.findActiveByAreaIdAndEquipmentManager(areaId, userId, Utility.getCurrLocalDateTime())
                .orElseThrow(NotAccessException::new);

        Area foundArea = trip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        if (!foundArea.getOwnerId().equals(userId) &&
                (foundArea.getEquipmentManagers() == null || !foundArea.getEquipmentManagers().contains(userId))
        )
            throw new NotAccessException();

        return new PairValue(foundArea, trip.getName());
    }

    //    @Transactional
    synchronized
    public void returnAllEquipments(
            ObjectId userId, String username,
            ObjectId areaId, ObjectId areaOwnerId
    ) {
        PairValue p = checkAccess(areaOwnerId != null ? areaOwnerId : userId, areaId);
        Area foundArea = (Area) p.getKey();
        doReturnAllEquipments(
                userId, username, areaId,
                foundArea.getName(), p.getValue().toString()
        );
    }

    public void returnAllEquipmentsByAdmin(
            ObjectId userId, String username,
            ObjectId areaId, String areaName,
            String tripName
    ) {
        doReturnAllEquipments(
                userId, username, areaId,
                areaName, tripName
        );
    }

    synchronized
    public void doReturnAllEquipments(
            ObjectId userId, String username,
            ObjectId areaId, String areaName,
            String tripName
    ) {
        List<AreaEquipments> areaEquipments = areaEquipmentsRepository.findAvailableEquipmentsByAreaId(areaId);
        if (areaEquipments.size() == 0)
            return;

        List<Equipment> equipments = new ArrayList<>();
        equipmentRepository.findAllById(areaEquipments.stream()
                .map(AreaEquipments::getEquipmentId)
                .collect(Collectors.toList())
        ).forEach(equipments::add);

        final String msg = "عودت از منطقه " + areaName + " در اردو " + tripName + " توسط " + username;
        List<EquipmentLog> equipmentLogs = new ArrayList<>();

        areaEquipments.forEach(areaEquipments1 -> equipments.stream().filter(equipment -> equipment.getId().equals(areaEquipments1.getEquipmentId()))
                .findFirst().ifPresent(equipment -> {
                    equipment.setAvailable(equipment.getAvailable() + areaEquipments1.getReminder());
                    equipmentLogs.add(
                            EquipmentLog
                                    .builder()
                                    .equipmentId(equipment.getId())
                                    .userId(userId)
                                    .areaId(areaId)
                                    .amount(areaEquipments1.getReminder())
                                    .desc(msg)
                                    .build()
                    );
                    areaEquipments1.setReminder(0);
                    areaEquipments1.setUpdatedAt(LocalDateTime.now());
                }));

        equipmentLogRepository.saveAll(equipmentLogs);
        equipmentRepository.saveAll(equipments);
        areaEquipmentsRepository.saveAll(areaEquipments);
    }

    public ResponseEntity<List<JoinedAreaEquipments>> list(ObjectId userId, ObjectId areaId) {

        tripRepository.findActiveByAreaIdAndEquipmentManager(areaId, userId, Utility.getCurrLocalDateTime())
                .orElseThrow(NotAccessException::new);

        return new ResponseEntity<>(
                areaEquipmentsRepository.findDigestByAreaId(areaId),
                HttpStatus.OK
        );
    }

    public ResponseEntity<Boolean> checkAccessToWareHouse(ObjectId groupId, ObjectId userId) {
        return new ResponseEntity<>(
                wareHouseAccessForGroupRepository.existsEquipmentAccessByGroupIdAndUserId(
                        groupId, userId
                ), HttpStatus.OK
        );
    }

    synchronized
    public void countDownEquipment(
            ObjectId userId, ObjectId areaId,
            ObjectId equipmentId, int count
    ) {
        Trip trip = tripRepository.findActiveByAreaIdAndEquipmentManager(
                areaId, userId, Utility.getCurrLocalDateTime()
        ).orElseThrow(InvalidIdException::new);
        AreaUtils.findStartedArea(trip, areaId);

        equipmentsInAreaRepositoryImp.countDown(areaId, equipmentId, count);
    }
}
