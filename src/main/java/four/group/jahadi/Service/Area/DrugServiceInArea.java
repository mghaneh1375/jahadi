package four.group.jahadi.Service.Area;

import four.group.jahadi.DTO.Area.AdviceDrugData;
import four.group.jahadi.DTO.Area.AreaDrugsData;
import four.group.jahadi.DTO.Area.GiveDrugData;
import four.group.jahadi.Enums.Module.DeliveryStatus;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Area.AreaDrugs;
import four.group.jahadi.Models.Area.JoinedAreaDrugs;
import four.group.jahadi.Models.Area.ModuleInArea;
import four.group.jahadi.Models.*;
import four.group.jahadi.Repository.Area.DrugsInAreaRepository;
import four.group.jahadi.Repository.Area.PatientsDrugRepository;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Repository.*;
import four.group.jahadi.Utility.PairValue;
import four.group.jahadi.Utility.Utility;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DrugServiceInArea {

    private final static Integer PAGE_SIZE = 20;
    @Autowired
    private WareHouseAccessForGroupRepository wareHouseAccessForGroupRepository;
    @Autowired
    private DrugRepository drugRepository;
    @Autowired
    private DrugLogRepository drugLogRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private DrugsInAreaRepository drugsInAreaRepository;
    @Autowired
    private PatientsDrugRepository patientsDrugRepository;
    @Autowired
    private PatientsInAreaRepository patientsInAreaRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;

    private static List<ObjectId> locks = new ArrayList<>();

    // ###################### GROUP ACCESS #######################

//    @Transactional
//    synchronized
    public void addAllToDrugsList(
            ObjectId userId, ObjectId groupId,
            String username, ObjectId areaId, List<AreaDrugsData> dtoList,
            boolean isGroupOwner
    ) {
        if (!isGroupOwner &&
                !wareHouseAccessForGroupRepository.existsDrugAccessByGroupIdAndUserId(
                        groupId, userId
                )
        )
            throw new NotAccessException();

        Trip trip = tripRepository.findActiveAreaByGroupIdAndAreaIdAndWriteAccess(Utility.getCurrDate(), groupId, areaId)
                .orElseThrow(NotAccessException::new);
        Area foundArea = trip.getAreas().stream()
                .filter(area -> area.getId().equals(areaId))
                .findFirst().get();

        List<ObjectId> ids = dtoList.stream().map(AreaDrugsData::getDrugId).distinct()
                .collect(Collectors.toList());
        List<Drug> drugsIter = drugRepository.findAllByIdsAndUserId(ids, isGroupOwner ? userId : null);

        List<AreaDrugs> drugs = new ArrayList<>();
        List<ObjectId> existDrugs = drugsInAreaRepository.findDrugIdsByAreaIdAndDrugIds(areaId, ids)
                .stream().map(AreaDrugs::getDrugId).collect(Collectors.toList());
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

        if (drugs.size() + updates.size() != dtoList.size())
            throw new RuntimeException("ids are incorrect");

        List<AreaDrugs> drugsInAreaList = drugsInAreaRepository.findByAreaIdAndDrugIds(areaId, updates.keySet().stream().toList());
        drugsInAreaList.forEach(next -> {
            next.setReminder(updates.get(next.getDrugId()) + next.getReminder());
            next.setTotalCount(updates.get(next.getDrugId()) + next.getTotalCount());
            next.setUpdatedAt(new Date());
        });

        drugRepository.saveAll(drugsIter);
        drugLogRepository.saveAll(drugLogs);
        drugsInAreaRepository.insert(drugs);
        drugsInAreaRepository.saveAll(drugsInAreaList);
    }

    @Transactional
    synchronized
    public void removeAllFromDrugsList(
            ObjectId userId, ObjectId groupId,
            String username, ObjectId areaId,
            List<ObjectId> ids, boolean isGroupOwner
    ) {
        if (!isGroupOwner &&
                !wareHouseAccessForGroupRepository.existsDrugAccessByGroupIdAndUserId(
                        groupId, userId
                )
        )
            throw new NotAccessException();

        Trip trip = tripRepository.findActiveAreaByGroupIdAndAreaIdAndWriteAccess(
                Utility.getCurrDate(), groupId, areaId
        ).orElseThrow(NotAccessException::new);
        Area foundArea = trip.getAreas().stream()
                .filter(area -> area.getId().equals(areaId))
                .findFirst().get();

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

    public ResponseEntity<List<JoinedAreaDrugs>> list(ObjectId userId, ObjectId areaId) {
        tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        return new ResponseEntity<>(
                drugsInAreaRepository.findDigestByAreaId(areaId),
                HttpStatus.OK
        );
    }

//    public void updateDrugReminder(
//            ObjectId userId, ObjectId areaId,
//            ObjectId id, Integer newReminder
//    ) {
//        PairValue p = checkAccess(userId, areaId);
//        Area foundArea = (Area) p.getKey();
//        String tripName = p.getValue().toString();
//        AreaDrugs areaDrug = drugsInAreaRepository.findById(id).orElseThrow(InvalidIdException::new);
//        int diff = newReminder - areaDrug.getReminder();
//        Drug drug = drugRepository.findById(areaDrug.getDrugId()).orElseThrow(InvalidIdException::new);
//
//        if (diff > 0) { // check availability
//            if (drug.getAvailable() < diff)
//                throw new RuntimeException("ظرفیت این دارو کمتر از مقدار درخواستی شما می باشد");
//        }
//
//        // todo: synchronize action
//        areaDrug.setReminder(newReminder);
//        drugsInAreaRepository.save(areaDrug);
//
//        drug.setAvailable(drug.getAvailable() + diff);
//        drugRepository.save(drug);
//
//        drugLogRepository.save(
//                DrugLog
//                        .builder()
//                        .drugId(drug.getId())
//                        .amount(diff)
//                        .desc("تغییر موجودی دارو در منطقه " + foundArea.getName() + " در اردو " + tripName + " توسط مسئول منطقه ")
//                        .build()
//        );
//    }

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

    private boolean lock(ObjectId areaDrugId) {
        if(locks.contains(areaDrugId)) {
            int retry = 0;
            while (retry < 10) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                retry++;
            }
            if(locks.contains(areaDrugId))
                throw new RuntimeException("Lock exception");

            return true;
        }
        return false;
    }

    synchronized
    public void advice(
            ObjectId userId, ObjectId patientId,
            ObjectId moduleId, ObjectId areaDrugId,
            AdviceDrugData data
    ) {
        lock(areaDrugId);
        AreaDrugs areaDrug = drugsInAreaRepository
                .findById(areaDrugId)
                .orElseThrow(InvalidIdException::new);

        locks.add(areaDrugId);
        try {
            if (areaDrug.getReminder() < data.getAmount())
                throw new RuntimeException("تعداد داروهای باقی مانده کمتر از تعداد درخواستی شما می باشد. تعداد باقی مانده: " + areaDrug.getReminder());

            if (!patientsInAreaRepository.existByAreaIdAndPatientId(areaDrug.getAreaId(), patientId))
                throw new RuntimeException("بیمار موردنظر در منطقه مدنظر یافت نشد");

            Trip trip = tripRepository.findByAreaIdAndResponsibleIdAndModuleId(
                    areaDrug.getAreaId(), userId, moduleId
            ).orElseThrow(NotAccessException::new);
            ModuleInArea moduleInArea = AreaUtils.findModule(
                    AreaUtils.findStartedArea(trip, areaDrug.getAreaId()),
                    moduleId, userId
            );

            patientsDrugRepository.insert(
                    PatientDrug
                            .builder()
                            .areaId(areaDrug.getAreaId())
                            .drugId(areaDrug.getDrugId())
                            .drugName(areaDrug.getDrugName())
                            .doctorId(userId)
                            .patientId(patientId)
                            .moduleId(moduleId)
                            .moduleName(moduleInArea.getModuleName())
                            .suggestCount(data.getAmount())
                            .amountOfUses(data.getAmountOfUse())
                            .howToUses(data.getHowToUse())
                            .useTime(data.getUseTime())
                            .description(data.getDescription())
                            .build()
            );
            locks.remove(areaDrugId);
        }
        catch (Exception x) {
            locks.remove(areaDrugId);
            throw x;
        }
    }

    public void removeAdvice(ObjectId userId, ObjectId adviceId) {
        patientsDrugRepository.deleteUnDedicatedPatientDrugByIdAAndDoctorId(
                adviceId, userId
        ).orElseThrow(() -> {
            throw new RuntimeException("این تجویز تحویل شده است و امکان حذف آن وجود ندارد");
        });
    }

    @Transactional
    synchronized
    public void giveDrug(
            ObjectId userId, ObjectId areaId,
            ObjectId adviceId, GiveDrugData data
    ) {
        Trip trip = tripRepository.findActiveByAreaIdAndPharmacyManager(
                areaId, userId, Utility.getCurrDate()
        ).orElseThrow(InvalidIdException::new);
        AreaUtils.findStartedArea(trip, areaId);
        PatientDrug patientDrug = patientsDrugRepository.findById(adviceId)
                .orElseThrow(InvalidIdException::new);

        if (!patientDrug.getAreaId().equals(areaId))
            throw new InvalidIdException();
        if (patientDrug.getSuggestCount() < data.getAmount())
            throw new RuntimeException("تعداد تحویل می تواند حداکثر " + patientDrug.getSuggestCount() + " باشد");
        if (patientDrug.isDedicated() && !patientDrug.getGiverId().equals(userId))
            throw new RuntimeException("این تجویز قبلا توسط مسئول داروخانه دیگری تحویل شده است");

        int diff;
        if (!patientDrug.isDedicated()) {
            patientDrug.setDedicated(true);
            patientDrug.setGiveAt(new Date());
            diff = data.getAmount() - (
                    patientDrug.getGiveCount() == null
                            ? 0
                            : patientDrug.getGiveCount()
            );
        } else
            diff = data.getAmount();

        ObjectId drugId = data.getDrugId() != null && !data.getDrugId().equals(patientDrug.getDrugId()) ?
                data.getDrugId() : patientDrug.getDrugId();
        AreaDrugs areaDrugs = drugsInAreaRepository.findByAreaIdAndDrugId(areaId, drugId)
                .orElseThrow(() -> {
                    throw new RuntimeException("unknown exception");
                });
        if(lock(areaDrugs.getId()))
            areaDrugs = drugsInAreaRepository.findById(areaDrugs.getId()).get();

        try {
            if (areaDrugs.getReminder() - diff < 0)
                throw new RuntimeException("مقدار موجودی در انبار " + areaDrugs.getReminder() + " می باشد");

            patientDrug.setGiveCount(data.getAmount());
            patientDrug.setGiverId(userId);
            patientDrug.setGiveDescription(data.getDescription());


            if (!drugId.equals(patientDrug.getDrugId())) {
                patientDrug.setGivenDrugId(drugId);
                patientDrug.setGivenDrugName(areaDrugs.getDrugName());
            }

            areaDrugs.setReminder(areaDrugs.getReminder() - diff);
            drugsInAreaRepository.save(areaDrugs);
            patientsDrugRepository.save(patientDrug);
            locks.remove(areaDrugs.getId());
        }
        catch (Exception x) {
            locks.remove(areaDrugs.getId());
            throw x;
        }
    }

    public ResponseEntity<List<PatientDrug>> listOfAdvices(
            ObjectId userId, ObjectId areaId,
            ObjectId patientId, ObjectId moduleId,
            ObjectId doctorId, DeliveryStatus deliveryStatus,
            ObjectId drugId, Date startAdviceAt, Date endAdviceAt,
            Date startGiveAt, Date endGiveAt,
            Integer startSuggestCount, Integer endSuggestCount,
            ObjectId giverId, Integer pageNo
    ) {
        Trip trip;

        if (patientId == null) {
            trip = tripRepository.findActiveByAreaIdAndPharmacyManager(
                    areaId, userId, Utility.getCurrDate()
            ).orElseThrow(NotAccessException::new);
        } else {
            trip = tripRepository.findActiveByAreaIdAndResponsibleId(
                    areaId, userId, Utility.getCurrDate()
            ).orElseThrow(NotAccessException::new);
        }
        AreaUtils.findStartedArea(trip, areaId);

        List<PatientDrug> patientsDrugs = patientsDrugRepository.findByFilters(
                areaId, patientId, moduleId,
                doctorId,
                deliveryStatus == null ? null : Objects.equals(DeliveryStatus.DELIVERED, deliveryStatus),
                drugId, startAdviceAt, endAdviceAt,
                startGiveAt, endGiveAt,
                startSuggestCount, endSuggestCount,
                giverId,
                patientId == null
                        ? (pageNo - 1) * PAGE_SIZE
                        : 0,
                patientId == null
                        ? PAGE_SIZE
                        : 100
        );
        if (patientId == null) {
            List<Patient> patients = patientRepository.findPublicInfoByIdIn(
                    patientsDrugs.stream().map(PatientDrug::getPatientId)
                            .distinct().collect(Collectors.toList())
            );
            patientsDrugs.forEach(patientDrug -> patients.stream()
                    .filter(patient -> patient.getId().equals(patientDrug.getPatientId()))
                    .findFirst()
                    .ifPresent(patientDrug::setPatient));
        }

        return new ResponseEntity<>(patientsDrugs, HttpStatus.OK);
    }

    public ResponseEntity<PatientDrug> getAdviceDetail(
            ObjectId userId, ObjectId adviceId
    ) {
        PatientDrug patientDrug = patientsDrugRepository.findById(adviceId)
                .orElseThrow(InvalidIdException::new);
        Trip trip = tripRepository.findByAreaIdAndResponsibleId(
                patientDrug.getAreaId(), userId
        ).orElseThrow(NotAccessException::new);
        AreaUtils.findStartedArea(trip, patientDrug.getAreaId());
        userRepository.findById(patientDrug.getDoctorId())
                .ifPresent(user -> patientDrug.setDoctor(user.getName()));
        if (patientDrug.getGiverId() != null) {
            userRepository.findById(patientDrug.getGiverId())
                    .ifPresent(user -> patientDrug.setGiver(user.getName()));
        }
        return new ResponseEntity<>(patientDrug, HttpStatus.OK);
    }

    @Transactional
    public void returnAllDrugs(ObjectId userId, String username, ObjectId areaId) {
        PairValue p = checkAccess(userId, areaId);
        Area foundArea = (Area) p.getKey();
        String tripName = p.getValue().toString();
        List<AreaDrugs> areaDrugs = drugsInAreaRepository.findAvailableDrugsByAreaId(areaId);
        if(areaDrugs.size() == 0)
            return;

        List<Drug> drugs = new ArrayList<>();
        drugRepository.findAllById(areaDrugs.stream()
                .map(AreaDrugs::getDrugId)
                .collect(Collectors.toList())
        ).forEach(drugs::add);

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

        drugLogRepository.saveAll(drugLogs);
        drugRepository.saveAll(drugs);
        drugsInAreaRepository.saveAll(areaDrugs);
    }
}
