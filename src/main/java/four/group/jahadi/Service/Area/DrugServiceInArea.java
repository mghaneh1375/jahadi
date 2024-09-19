package four.group.jahadi.Service.Area;

import four.group.jahadi.DTO.Area.AreaDrugsData;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Area.AreaDrugs;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Models.DrugLog;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Repository.Area.DrugsInAreaRepository;
import four.group.jahadi.Repository.DrugLogRepository;
import four.group.jahadi.Repository.DrugRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Utility.PairValue;
import four.group.jahadi.Utility.Utility;
import lombok.Synchronized;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DrugServiceInArea {

    @Autowired
    private DrugRepository drugRepository;

    @Autowired
    private DrugLogRepository drugLogRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private DrugsInAreaRepository drugsInAreaRepository;

    // ###################### GROUP ACCESS #######################

    @Synchronized
    public void addAllToDrugsList(
            ObjectId userId, ObjectId groupId,
            String username, ObjectId areaId, List<AreaDrugsData> dtoList
    ) {

        Trip trip = tripRepository.findActiveAreaByGroupIdAndAreaIdAndWriteAccess(Utility.getCurrDate(), groupId, areaId)
                .orElseThrow(NotAccessException::new);
        Area foundArea = trip.getAreas().stream()
                .filter(area -> area.getId().equals(areaId))
                .findFirst().get();

        List<ObjectId> ids = dtoList.stream().map(AreaDrugsData::getDrugId).distinct()
                .collect(Collectors.toList());
        List<Drug> drugsIter = drugRepository.findAllByIdsAndUserId(ids, userId);

        List<AreaDrugs> drugs = new ArrayList<>();
        List<ObjectId> existDrugs = drugsInAreaRepository.findIdsByAreaIdAndIds(areaId, ids);
        HashMap<ObjectId, Integer> updates = new HashMap<>();
        existDrugs.forEach(objectId -> updates.put(objectId, 0));

        List<DrugLog> drugLogs = new ArrayList<>();
        final String msg = "اختصاص به منطقه " + foundArea.getName() + " در اردو " + trip.getName() + " توسط " + username;

        for (Drug drug : drugsIter) {
            dtoList.stream()
                    .filter(areaDrugsData -> areaDrugsData.getDrugId().equals(drug.getId()))
                    .findFirst().ifPresent(dto -> {

                        if (drug.getAvailable() < dto.getTotalCount())
                            throw new RuntimeException("داروی " + drug.getName() + "ظرفیت این دارو کمتر از مقدار درخواستی شما می باشد");

                        if (existDrugs.contains(drug.getId()))
                            updates.put(drug.getId(), updates.get(drug.getId()) + dto.getTotalCount());
                        else
                            drugs.add(
                                    AreaDrugs
                                            .builder()
                                            .drugName(drug.getName())
                                            .drugId(drug.getId())
                                            .areaId(areaId)
                                            .totalCount(dto.getTotalCount())
                                            .reminder(dto.getTotalCount())
                                            .updatedAt(new Date())
                                            .build()
                            );

                        drug.setAvailable(drug.getAvailable() - dto.getTotalCount());
                        drugLogs.add(
                                DrugLog
                                        .builder()
                                        .drugId(drug.getId())
                                        .userId(userId)
                                        .areaId(areaId)
                                        .amount(-dto.getTotalCount())
                                        .desc(msg)
                                        .build()
                        );
                    });
        }

        if (drugs.size() != dtoList.size())
            throw new RuntimeException("ids are incorrect");

        Iterable<AreaDrugs> drugsInAreaList = drugsInAreaRepository.findAllById(updates.keySet());
        List<AreaDrugs> tmp = new ArrayList<>();
        while (drugsInAreaList.iterator().hasNext()) {
            AreaDrugs next = drugsInAreaList.iterator().next();
            next.setReminder(updates.get(next.getDrugId()) + next.getReminder());
            next.setTotalCount(updates.get(next.getDrugId()) + next.getTotalCount());
            next.setUpdatedAt(new Date());
            tmp.add(next);
        }

        //todo: synchronized op
        drugRepository.saveAll(drugsIter);
        drugLogRepository.saveAll(drugLogs);
        drugsInAreaRepository.insert(drugs);
        drugsInAreaRepository.saveAll(tmp);
    }

    @Synchronized
    public void removeAllFromDrugsList(
            ObjectId userId, ObjectId groupId,
            String username, ObjectId areaId,
            List<ObjectId> ids
    ) {

        Trip trip = tripRepository.findActiveAreaByGroupIdAndAreaIdAndWriteAccess(
                Utility.getCurrDate(), groupId, areaId
        ).orElseThrow(NotAccessException::new);
        Area foundArea = trip.getAreas().stream()
                .filter(area -> area.getId().equals(areaId))
                .findFirst().get();

        //todo: synchronized op
        List<AreaDrugs> areaDrugs = drugsInAreaRepository.removeAreaDrugsByIdAndAreaId(ids, areaId);
        List<DrugLog> drugLogs = new ArrayList<>();

        List<Drug> drugsIter = drugRepository.findAllByIdsAndUserId(
                areaDrugs.stream().map(AreaDrugs::getDrugId).collect(Collectors.toList()),
                userId
        );
        final String msg = "حذف از منطقه " + foundArea.getName() + " در اردو " + trip.getName() + " توسط " + username;

        for (Drug drug : drugsIter) {
            areaDrugs.stream().filter(areaDrug -> areaDrug.getDrugId().equals(drug.getId())).findFirst()
                    .ifPresent(areaDrug -> {
                        drug.setAvailable(drug.getAvailable() + areaDrug.getReminder());
                        drugLogs.add(
                                DrugLog
                                        .builder()
                                        .drugId(drug.getId())
                                        .userId(userId)
                                        .areaId(areaId)
                                        .amount(areaDrug.getReminder())
                                        .desc(msg)
                                        .build()
                        );
                    });
        }

        drugRepository.saveAll(drugsIter);
        drugLogRepository.saveAll(drugLogs);
    }

    // ###################### JAHADGAR ACCESS #######################

    public ResponseEntity<List<AreaDrugs>> list(ObjectId userId, ObjectId areaId) {

        tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        return new ResponseEntity<>(
                drugsInAreaRepository.findDigestByAreaId(areaId),
                HttpStatus.OK
        );
    }

    public void updateDrugReminder(
            ObjectId userId, ObjectId areaId,
            ObjectId id, Integer newReminder
    ) {
        PairValue p = checkAccess(userId, areaId);
        Area foundArea = (Area) p.getKey();
        String tripName = p.getValue().toString();
        AreaDrugs areaDrug = drugsInAreaRepository.findById(id).orElseThrow(InvalidIdException::new);
        int diff = newReminder - areaDrug.getReminder();
        Drug drug = drugRepository.findById(areaDrug.getDrugId()).orElseThrow(InvalidIdException::new);

        if (diff > 0) { // check availability
            if (drug.getAvailable() < diff)
                throw new RuntimeException("ظرفیت این دارو کمتر از مقدار درخواستی شما می باشد");
        }

        // todo: synchronize action
        areaDrug.setReminder(newReminder);
        drugsInAreaRepository.save(areaDrug);

        drug.setAvailable(drug.getAvailable() + diff);
        drugRepository.save(drug);

        drugLogRepository.save(
                DrugLog
                        .builder()
                        .drugId(drug.getId())
                        .amount(diff)
                        .desc("تغییر موجودی دارو در منطقه " + foundArea.getName() + " در اردو " + tripName + " توسط مسئول منطقه ")
                        .build()
        );
    }

    private PairValue checkAccess(ObjectId userId, ObjectId areaId) {
        Trip trip = tripRepository.findActiveByAreaIdAndPharmacyManager(areaId, userId, Utility.getCurrDate())
                .orElseThrow(NotAccessException::new);

        Area foundArea = trip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        if (!foundArea.getOwnerId().equals(userId) &&
                (foundArea.getPharmacyManagers() == null || !foundArea.getPharmacyManagers().contains(userId))
        )
            throw new NotAccessException();

        return new PairValue(foundArea, trip.getName());
    }

    public void advice(ObjectId areaDrugId, Integer amount) {

        AreaDrugs areaDrug = drugsInAreaRepository
                .findById(areaDrugId)
                .orElseThrow(InvalidIdException::new);

        if (areaDrug.getReminder() < amount)
            throw new RuntimeException("تعداد داروهای باقی مانده کمتر از تعداد درخواستی شما می باشد");

        areaDrug.setReminder(areaDrug.getReminder() - amount);
        drugsInAreaRepository.save(areaDrug);
    }

    public void returnAllDrugs(ObjectId userId, String username, ObjectId areaId) {
        PairValue p = checkAccess(userId, areaId);
        Area foundArea = (Area) p.getKey();
        String tripName = p.getValue().toString();
        List<AreaDrugs> areaDrugs = drugsInAreaRepository.findAvailableDrugsByAreaId(areaId);
        List<Drug> drugs = drugRepository.findByIds(areaDrugs.stream().map(AreaDrugs::getDrugId).collect(Collectors.toList()));
        Date curr = new Date();
        final String msg = "عودت از منطقه " + foundArea.getName() + " در اردو " + tripName + " توسط " + username;
        List<DrugLog> drugLogs = new ArrayList<>();

        areaDrugs.forEach(areaDrugs1 -> drugs.stream().filter(drug -> drug.getId().equals(areaDrugs1.getDrugId()))
                .findFirst().ifPresent(drug -> {
                    drug.setAvailable(drug.getAvailable() + areaDrugs1.getReminder());
                    drugLogs.add(
                            DrugLog
                                    .builder()
                                    .drugId(drug.getId())
                                    .userId(userId)
                                    .areaId(areaId)
                                    .amount(areaDrugs1.getReminder())
                                    .desc(msg)
                                    .build()
                    );
                    areaDrugs1.setReminder(0);
                    areaDrugs1.setUpdatedAt(curr);
                }));

        // todo: transaction
        drugLogRepository.saveAll(drugLogs);
        drugRepository.saveAll(drugs);
        drugsInAreaRepository.saveAll(areaDrugs);
    }
}
