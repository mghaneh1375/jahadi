package four.group.jahadi.Service;

import four.group.jahadi.DTO.Area.AreaEquipmentsData;
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
import java.util.HashMap;
import java.util.Iterator;
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
    public void addAllEquipmentsToArea(
            ObjectId userId, ObjectId groupId,
            String username, ObjectId areaId,
            List<AreaEquipmentsData> dtoList,
            boolean isGroupOwner
    ) {
        if (!isGroupOwner &&
                !wareHouseAccessForGroupRepository.existsEquipmentAccessByGroupIdAndUserId(
                        groupId, userId)
        )
            throw new NotAccessException();

        Trip trip = tripRepository.findActiveAreaByGroupIdAndAreaIdAndWriteAccess(Utility.getCurrLocalDateTime(), groupId, areaId)
                .orElseThrow(NotAccessException::new);
        Area foundArea = trip.getAreas().stream()
                .filter(area -> area.getId().equals(areaId))
                .findFirst().get();

        List<ObjectId> ids = dtoList.stream().map(AreaEquipmentsData::getEquipmentId).distinct()
                .collect(Collectors.toList());
        List<Equipment> equipmentsIter = equipmentRepository.findAllByIdsAndGroupId(ids, groupId);

        List<AreaEquipments> equipments = new ArrayList<>();
        HashMap<ObjectId, Integer> updates = new HashMap<>();
        areaEquipmentsRepository.findIdsByAreaIdAndIds(areaId, ids)
                .forEach(areaEquipment -> updates.put(areaEquipment.getEquipmentId(), 0));

        List<EquipmentLog> equipmentLogs = new ArrayList<>();
        final String msg = "اختصاص به منطقه " + foundArea.getName() + " در اردو " + trip.getName() + " توسط " + username;

        for (Equipment equipment : equipmentsIter) {
            dtoList.stream()
                    .filter(areaEquipmentsData -> areaEquipmentsData.getEquipmentId().equals(equipment.getId()))
                    .findFirst().ifPresent(dto -> {

                        if (equipment.getAvailable() < dto.getTotalCount())
                            throw new RuntimeException("تجهیز " + equipment.getName() + "ظرفیت این تجهیز کمتر از مقدار درخواستی شما می باشد");

                        if (updates.containsKey(equipment.getId()))
                            updates.put(equipment.getId(), updates.get(equipment.getId()) + dto.getTotalCount());
                        else
                            equipments.add(
                                    AreaEquipments
                                            .builder()
                                            .equipmentName(equipment.getName())
                                            .equipmentId(equipment.getId())
                                            .areaId(areaId)
                                            .totalCount(dto.getTotalCount())
                                            .reminder(dto.getTotalCount())
                                            .updatedAt(LocalDateTime.now())
                                            .build()
                            );

                        equipment.setAvailable(equipment.getAvailable() - dto.getTotalCount());
                        equipmentLogs.add(
                                EquipmentLog
                                        .builder()
                                        .equipmentId(equipment.getId())
                                        .userId(userId)
                                        .areaId(areaId)
                                        .amount(-dto.getTotalCount())
                                        .desc(msg)
                                        .build()
                        );
                    });
        }

        if (equipments.size() + updates.size() != dtoList.size())
            throw new RuntimeException("ids are incorrect");

        Iterable<AreaEquipments> equipmentsInAreaList = areaEquipmentsRepository.findAllByAreaIdAndEquipmentId(areaId, updates.keySet());
        List<AreaEquipments> tmp = new ArrayList<>();
        Iterator<AreaEquipments> iterator = equipmentsInAreaList.iterator();
        while (iterator.hasNext()) {
            AreaEquipments next = iterator.next();
            next.setReminder(updates.get(next.getEquipmentId()) + next.getReminder());
            next.setTotalCount(updates.get(next.getEquipmentId()) + next.getTotalCount());
            next.setUpdatedAt(LocalDateTime.now());
            tmp.add(next);
        }

        equipmentRepository.saveAll(equipmentsIter);
        equipmentLogRepository.saveAll(equipmentLogs);
        areaEquipmentsRepository.insert(equipments);
        areaEquipmentsRepository.saveAll(tmp);
    }

    //    @Transactional
    synchronized
    public void removeAllFromEquipmentsList(
            ObjectId userId, ObjectId groupId,
            String username, ObjectId areaId,
            List<ObjectId> ids, boolean isGroupOwner
    ) {
        if (!isGroupOwner &&
                !wareHouseAccessForGroupRepository.existsEquipmentAccessByGroupIdAndUserId(
                        groupId, userId)
        )
            throw new NotAccessException();

        Trip trip = tripRepository.findActiveAreaByGroupIdAndAreaIdAndWriteAccess(
                Utility.getCurrLocalDateTime(), groupId, areaId
        ).orElseThrow(NotAccessException::new);
        Area foundArea = trip.getAreas().stream()
                .filter(area -> area.getId().equals(areaId))
                .findFirst().get();

        List<AreaEquipments> areaEquipments = areaEquipmentsRepository.removeAreaEquipmentsByIdAndAreaId(ids, areaId);
        List<EquipmentLog> equipmentLogs = new ArrayList<>();
        List<Equipment> equipmentsIter = equipmentRepository.findAllByIdsAndGroupId(
                areaEquipments.stream().map(AreaEquipments::getEquipmentId).collect(Collectors.toList()),
                groupId
        );
        final String msg = "حذف از منطقه " + foundArea.getName() + " در اردو " + trip.getName() + " توسط " + username;

        for (Equipment equipment : equipmentsIter) {
            areaEquipments.stream().filter(areaEquipment -> areaEquipment.getEquipmentId().equals(equipment.getId())).findFirst()
                    .ifPresent(areaEquipment -> {
                        equipment.setAvailable(equipment.getAvailable() + areaEquipment.getReminder());
                        equipmentLogs.add(
                                EquipmentLog
                                        .builder()
                                        .equipmentId(equipment.getId())
                                        .userId(userId)
                                        .areaId(areaId)
                                        .amount(areaEquipment.getReminder())
                                        .desc(msg)
                                        .build()
                        );
                    });
        }

        equipmentRepository.saveAll(equipmentsIter);
        equipmentLogRepository.saveAll(equipmentLogs);
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

    synchronized
    public void countDownEquipmentByGroup(
            ObjectId groupId, ObjectId areaId, ObjectId userId,
            ObjectId equipmentId, int count
    ) {
        if (userId != null)
            checkAccessToWareHouse(groupId, userId);

        tripRepository.findByGroupIdAndAreaId(groupId, areaId)
                .orElseThrow(NotAccessException::new);

        equipmentsInAreaRepositoryImp.countDown(areaId, equipmentId, count);
    }

    public ResponseEntity<List<JoinedAreaEquipments>> list(
            ObjectId groupId, ObjectId areaId, ObjectId userId
    ) {
        if (userId != null)
            checkAccessToWareHouse(groupId, userId);

        tripRepository.findByGroupIdAndAreaId(groupId, areaId)
                .orElseThrow(NotAccessException::new);

        return new ResponseEntity<>(
                areaEquipmentsRepository.findDigestByAreaId(areaId),
                HttpStatus.OK
        );
    }
}
