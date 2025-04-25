package four.group.jahadi.Service.Area;

import four.group.jahadi.DTO.Area.AdviceDrugData;
import four.group.jahadi.DTO.Area.GiveDrugData;
import four.group.jahadi.DTO.Patient.PatientAdvices;
import four.group.jahadi.Enums.Module.DeliveryStatus;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Area.AreaDrugs;
import four.group.jahadi.Models.Area.JoinedAreaDrugs;
import four.group.jahadi.Models.Area.ModuleInArea;
import four.group.jahadi.Models.*;
import four.group.jahadi.Repository.Area.AreaDrugsRepository;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DrugServiceInArea {

    private final static Integer PAGE_SIZE = 20;
    @Autowired
    private DrugRepository drugRepository;
    @Autowired
    private DrugLogRepository drugLogRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private AreaDrugsRepository areaDrugsRepository;
    @Autowired
    private PatientsDrugRepository patientsDrugRepository;
    @Autowired
    private PatientsInAreaRepository patientsInAreaRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;

    private static final List<ObjectId> locks = new ArrayList<>();

    // ###################### JAHADGAR ACCESS #######################

    public ResponseEntity<List<JoinedAreaDrugs>> list(ObjectId userId, ObjectId areaId) {
        tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        return new ResponseEntity<>(
                areaDrugsRepository.findDigestByAreaId(areaId),
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
        if (locks.contains(areaDrugId)) {
            int retry = 0;
            while (retry < 10 && locks.contains(areaDrugId)) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                retry++;
            }
            if (locks.contains(areaDrugId))
                throw new RuntimeException("Lock exception");

            // unlocked by wait a few seconds, so reFetch from db
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
        AreaDrugs areaDrug = areaDrugsRepository
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
                            .amountOfUse(data.getAmountOfUse())
                            .howToUse(data.getHowToUse())
                            .useTime(data.getUseTime())
                            .description(data.getDescription())
                            .build()
            );
            locks.remove(areaDrugId);
        } catch (Exception x) {
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

    //    @Transactional
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
        AreaDrugs areaDrug = areaDrugsRepository.findByAreaIdAndDrugId(areaId, drugId)
                .orElseThrow(() -> {
                    throw new RuntimeException("unknown exception");
                });
        if (lock(areaDrug.getId()))
            areaDrug = areaDrugsRepository.findById(areaDrug.getId()).get();

        try {
            if (areaDrug.getReminder() - diff < 0)
                throw new RuntimeException("مقدار موجودی در انبار " + areaDrug.getReminder() + " می باشد");

            locks.add(areaDrug.getId());
            patientDrug.setGiveCount(data.getAmount());
            patientDrug.setGiverId(userId);
            patientDrug.setGiveDescription(data.getDescription());

            if (!drugId.equals(patientDrug.getDrugId())) {
                patientDrug.setGivenDrugId(drugId);
                patientDrug.setGivenDrugName(areaDrug.getDrugName());
            }

            areaDrug.setReminder(areaDrug.getReminder() - diff);
            areaDrugsRepository.save(areaDrug);
            patientsDrugRepository.save(patientDrug);
            locks.remove(areaDrug.getId());
        } catch (Exception x) {
            locks.remove(areaDrug.getId());
            throw x;
        }
    }

    public ResponseEntity<List<PatientAdvices>> listOfAdvices(
            ObjectId userId, ObjectId areaId,
            ObjectId patientId, ObjectId moduleId,
            ObjectId doctorId, DeliveryStatus deliveryStatus,
            ObjectId drugId, Date startAdviceAt, Date endAdviceAt,
            Date startGiveAt, Date endGiveAt,
            Integer startSuggestCount, Integer endSuggestCount,
            ObjectId giverId, Integer pageNo
    ) {
        Trip trip;
        List<PatientAdvices> output = new ArrayList<>();

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
        List<Patient> patients = patientRepository.findPublicInfoByIdIn(
                patientsDrugs.stream().map(PatientDrug::getPatientId)
                        .distinct().collect(Collectors.toList())
        );
        patients.forEach(patient -> output.add(PatientAdvices.builder().patient(patient).drugs(new ArrayList<>()).build()));
        patientsDrugs.forEach(patientDrug -> output.stream()
                .filter(patientAdvices -> patientAdvices.getPatient().getId().equals(patientDrug.getPatientId()))
                .findFirst()
                .ifPresent(patientAdvices -> patientAdvices.addToDrugList(patientDrug)));

        return new ResponseEntity<>(output, HttpStatus.OK);
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
        patientDrug.setUseTimeFa(patientDrug.getUseTime().getFaTranslate());
        patientDrug.setAmountOfUseFa(patientDrug.getAmountOfUse().getFaTranslate());
        patientDrug.setHowToUseFa(patientDrug.getHowToUse().getFaTranslate());
        return new ResponseEntity<>(patientDrug, HttpStatus.OK);
    }

    //    @Transactional
    public void returnAllDrugs(
            ObjectId userId, String username,
            ObjectId areaId
    ) {
        PairValue p = checkAccess(userId, areaId);
        Area foundArea = (Area) p.getKey();
        String tripName = p.getValue().toString();
        doReturnAllDrugs(foundArea.getName(), tripName, username, areaId, userId);
    }

    public void doReturnAllDrugs(
            String areaName, String tripName,
            String username, ObjectId areaId,
            ObjectId userId
    ) {
        List<AreaDrugs> areaDrugs = areaDrugsRepository.findAvailableDrugsByAreaId(areaId);
        if (areaDrugs.size() == 0)
            return;

        List<Drug> drugs = new ArrayList<>();
        drugRepository.findAllById(areaDrugs.stream()
                .map(AreaDrugs::getDrugId)
                .collect(Collectors.toList())
        ).forEach(drugs::add);

        Date curr = new Date();
        final String msg = "عودت از منطقه " + areaName + " در اردو " + tripName + " توسط " + username;
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
        areaDrugsRepository.saveAll(areaDrugs);
    }

    public void returnAllDrugsByAdmin(
            ObjectId userId, String username,
            ObjectId areaId, String areaName, String tripName
    ) {
        doReturnAllDrugs(areaName, tripName, username, areaId, userId);
    }
}
