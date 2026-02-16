package four.group.jahadi.Service.Area;

import four.group.jahadi.DTO.Area.AdviceDrugData;
import four.group.jahadi.DTO.Area.AreaDrugsData;
import four.group.jahadi.DTO.Area.GiveDrugData;
import four.group.jahadi.DTO.ErrorRow;
import four.group.jahadi.DTO.Patient.PatientAdvices;
import four.group.jahadi.Enums.Module.DeliveryStatus;
import four.group.jahadi.Exception.InvalidFieldsException;
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
import four.group.jahadi.Service.JahadgarDrugService;
import four.group.jahadi.Utility.PairValue;
import four.group.jahadi.Utility.Utility;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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
    private AreaDrugsRepository areaDrugsRepository;
    @Autowired
    private PatientsDrugRepository patientsDrugRepository;
    @Autowired
    private PatientsInAreaRepository patientsInAreaRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JahadgarDrugService jahadgarDrugService;

    private static final List<ObjectId> locks = new ArrayList<>();

    // ###################### GROUP ACCESS #######################

    //    @Transactional
    synchronized
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

        Trip trip = tripRepository.findActiveAreaByGroupIdAndAreaIdAndWriteAccess(Utility.getCurrLocalDateTime(), groupId, areaId)
                .orElseThrow(NotAccessException::new);
        Area foundArea = trip.getAreas().stream()
                .filter(area -> area.getId().equals(areaId))
                .findFirst().get();

        List<ObjectId> ids = dtoList.stream().map(AreaDrugsData::getDrugId).distinct()
                .collect(Collectors.toList());
        List<Drug> drugsIter = drugRepository.findAllByIdsAndGroupId(ids, groupId);

        List<AreaDrugs> drugs = new ArrayList<>();
        List<ObjectId> existDrugs = areaDrugsRepository.findDrugIdsByAreaIdAndDrugIds(areaId, ids)
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
                                            .updatedAt(LocalDateTime.now())
                                            .build()
                            );

                        drug.setAvailable(drug.getAvailable() - dto.getTotalCount());
                        drugLogs.add(
                                DrugLog
                                        .builder()
                                        .drugId(drug.getId())
                                        .userId(userId)
                                        .areaId(areaId)
                                        .groupId(groupId)
                                        .amount(-dto.getTotalCount())
                                        .desc(msg)
                                        .build()
                        );
                    });
        }

        if (drugs.size() + updates.size() != dtoList.size())
            throw new RuntimeException("ids are incorrect");

        List<AreaDrugs> drugsInAreaList = areaDrugsRepository.findByAreaIdAndDrugIds(areaId, new ArrayList<>(updates.keySet()));
        drugsInAreaList.forEach(next -> {
            next.setReminder(updates.get(next.getDrugId()) + next.getReminder());
            next.setTotalCount(updates.get(next.getDrugId()) + next.getTotalCount());
            next.setUpdatedAt(LocalDateTime.now());
        });

        drugRepository.saveAll(drugsIter);
        drugLogRepository.saveAll(drugLogs);
        areaDrugsRepository.insert(drugs);
        areaDrugsRepository.saveAll(drugsInAreaList);
    }

    private HashMap<String, Integer> calcValidRows(
            List<Row> rows,
            List<ErrorRow> errors
    ) {
        HashMap<String, Integer> output = new HashMap<>();
        for (Row row : rows) {
            try {
                if (
                        row.getCell(0) == null ||
                                row.getCell(0).getCellType() == CellType.BLANK ||
                                row.getCell(1) == null ||
                                row.getCell(1).getCellType() == CellType.BLANK
                )
                    continue;

                String key = row.getCell(0).getCellType().equals(CellType.NUMERIC)
                        ? (row.getCell(0).getNumericCellValue() + "").replace(".0", "")
                        : row.getCell(0).getStringCellValue();
                int value;

                if (row.getCell(1).getCellType().equals(CellType.NUMERIC))
                    value = (int) row.getCell(1).getNumericCellValue();
                else
                    value = Integer.parseInt(row.getCell(1).getStringCellValue());

                output.put(
                        key, output.getOrDefault(key, 0) + value
                );
            } catch (Exception ex) {
                errors.add(
                        ErrorRow
                                .builder()
                                .rowIndex(row.getRowNum())
                                .errorMsg(ex.getMessage())
                                .build()
                );
            }
        }

        return output;
    }

    public ResponseEntity<List<ErrorRow>> addBatchDrugsToList(
            ObjectId userId, ObjectId groupId,
            String username, ObjectId areaId,
            MultipartFile file, boolean isGroupOwner
    ) {
        if (file == null)
            throw new InvalidFieldsException("لطفا فایل را بارگذاری نمایید");

        Trip trip = tripRepository.findActiveAreaByGroupIdAndAreaIdAndWriteAccess(
                        Utility.getCurrLocalDateTime(), groupId, areaId
                ).orElseThrow(NotAccessException::new);
        Area foundArea = trip.getAreas().stream()
                .filter(area -> area.getId().equals(areaId))
                .findFirst().get();
        List<ErrorRow> errorRows = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            List<Row> rows = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++)
                rows.add(sheet.getRow(i));

            HashMap<String, Integer> data = calcValidRows(rows, errorRows);
            List<Drug> drugs = drugRepository.findIdsByCodes(data.keySet(), groupId);
            List<ObjectId> ids = new ArrayList<>();
            List<AreaDrugsData> areaDrugsDataList = drugs
                    .stream()
                    .map(drug -> {
                        ids.add(drug.getId());
                        AreaDrugsData areaDrugsData = new AreaDrugsData();
                        areaDrugsData.setDrugId(drug.getId());
                        areaDrugsData.setTotalCount(data.get(drug.getCode()));
                        return areaDrugsData;
                    }).collect(Collectors.toList());

            if (!isGroupOwner &&
                    !wareHouseAccessForGroupRepository.existsDrugAccessByGroupIdAndUserId(
                            groupId, userId
                    )
            )
                throw new NotAccessException();

            List<AreaDrugs> areaDrugs = new ArrayList<>();
            List<ObjectId> existDrugs = areaDrugsRepository
                    .findDrugIdsByAreaIdAndDrugIds(areaId, ids)
                    .stream().map(AreaDrugs::getDrugId)
                    .collect(Collectors.toList());

            HashMap<ObjectId, Integer> updates = new HashMap<>();
            existDrugs.forEach(objectId -> updates.put(objectId, 0));

            List<DrugLog> drugLogs = new ArrayList<>();
            final String msg = "اختصاص به منطقه " + foundArea.getName() + " در اردو " + trip.getName() + " توسط " + username;

            for (Drug drug : drugs) {
                areaDrugsDataList.stream()
                        .filter(areaDrugsData -> areaDrugsData.getDrugId().equals(drug.getId()))
                        .findFirst().ifPresent(dto -> {
                            if (drug.getAvailable() < dto.getTotalCount()) {
                                errorRows.add(
                                        ErrorRow
                                                .builder()
                                                .rowIndex(drug.getCode())
                                                .errorMsg("داروی " + drug.getName() + "ظرفیت این دارو کمتر از مقدار درخواستی شما می باشد")
                                                .build()
                                );
                                return;
                            }

                            if (existDrugs.contains(drug.getId()))
                                updates.put(drug.getId(), updates.get(drug.getId()) + dto.getTotalCount());
                            else {
                                areaDrugs.add(
                                        AreaDrugs
                                                .builder()
                                                .drugName(drug.getName())
                                                .drugId(drug.getId())
                                                .areaId(areaId)
                                                .totalCount(dto.getTotalCount())
                                                .reminder(dto.getTotalCount())
                                                .updatedAt(LocalDateTime.now())
                                                .build()
                                );
                            }

                            drug.setAvailable(drug.getAvailable() - dto.getTotalCount());
                            drugLogs.add(
                                    DrugLog
                                            .builder()
                                            .drugId(drug.getId())
                                            .userId(userId)
                                            .areaId(areaId)
                                            .groupId(groupId)
                                            .amount(-dto.getTotalCount())
                                            .desc(msg)
                                            .build()
                            );
                        });
            }

            List<AreaDrugs> drugsInAreaList = areaDrugsRepository.findByAreaIdAndDrugIds(
                    areaId, new ArrayList<>(updates.keySet())
            );
            drugsInAreaList.forEach(next -> {
                next.setReminder(updates.get(next.getDrugId()) + next.getReminder());
                next.setTotalCount(updates.get(next.getDrugId()) + next.getTotalCount());
                next.setUpdatedAt(LocalDateTime.now());
            });

            drugRepository.saveAll(drugs);
            drugLogRepository.saveAll(drugLogs);
            areaDrugsRepository.insert(areaDrugs);
            areaDrugsRepository.saveAll(drugsInAreaList);
        } catch (Exception x) {}

        return ResponseEntity.ok(errorRows);
    }

    //    @Transactional
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
                Utility.getCurrLocalDateTime(), groupId, areaId
        ).orElseThrow(NotAccessException::new);
        Area foundArea = trip.getAreas().stream()
                .filter(area -> area.getId().equals(areaId))
                .findFirst().get();

        List<AreaDrugs> areaDrugs = areaDrugsRepository.removeAreaDrugsByIdAndAreaId(ids, areaId);
        List<DrugLog> drugLogs = new ArrayList<>();

        List<Drug> drugsIter = drugRepository.findAllByIdsAndGroupId(
                areaDrugs.stream().map(AreaDrugs::getDrugId).collect(Collectors.toList()),
                groupId
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
                                        .groupId(groupId)
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

    public ResponseEntity<List<JoinedAreaDrugs>> search(
            ObjectId userId, ObjectId groupId, ObjectId areaId, String key
    ) {
        tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        List<JoinedAreaDrugs> digestByAreaId = drugRepository.searchInAreaByName(areaId, groupId, key);

        return new ResponseEntity<>(
                digestByAreaId,
                HttpStatus.OK
        );
    }

    public ResponseEntity<Page<JoinedAreaDrugs>> list(
            ObjectId userId, ObjectId areaId, Integer pageIndex, Integer pageSize
    ) {
        tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        List<JoinedAreaDrugs> digestByAreaId = areaDrugsRepository.findDigestByAreaId(areaId, pageIndex * pageSize, pageSize);

        return new ResponseEntity<>(
                new PageImpl<>(
                        digestByAreaId,
                        pageable,
                        areaDrugsRepository.countByAreaId(areaId)
                ),
                HttpStatus.OK
        );
    }

    public ResponseEntity<List<JoinedAreaDrugs>> list(
            ObjectId groupId, ObjectId areaId, ObjectId userId,
            Integer pageIndex, Integer pageSize,
            String name, String drugType,
            LocalDateTime fromExpireAt, LocalDateTime endExpireAt
    ) {
        if (userId != null)
            jahadgarDrugService.checkAccessToWareHouse(groupId, userId);

        tripRepository.findByGroupIdAndAreaId(groupId, areaId)
                .orElseThrow(NotAccessException::new);

        return new ResponseEntity<>(
                fromExpireAt == null && endExpireAt == null ?
                        areaDrugsRepository.findDigestByAreaId(areaId, pageIndex * pageSize, pageSize, name, drugType) :
                        fromExpireAt != null && endExpireAt != null ?
                                areaDrugsRepository.findDigestByAreaId(areaId, pageIndex * pageSize, pageSize, name, drugType, fromExpireAt, endExpireAt) :
                                fromExpireAt != null ?
                                        areaDrugsRepository.findDigestByAreaIdStartExpireAt(areaId, pageIndex * pageSize, pageSize, name, drugType, fromExpireAt) :
                                        areaDrugsRepository.findDigestByAreaIdEndExpireAt(areaId, pageIndex * pageSize, pageSize, name, drugType, endExpireAt),
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
        Trip trip = tripRepository.findActiveByAreaIdAndPharmacyManager(areaId, userId, Utility.getCurrLocalDateTime())
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
                areaId, userId, Utility.getCurrLocalDateTime()
        ).orElseThrow(InvalidIdException::new);
        AreaUtils.findStartedArea(trip, areaId);
        PatientDrug patientDrug = patientsDrugRepository.findById(adviceId)
                .orElseThrow(InvalidIdException::new);

        if (!patientDrug.getAreaId().equals(areaId))
            throw new InvalidIdException();
//        if (patientDrug.getSuggestCount() < data.getAmount())
//            throw new RuntimeException("تعداد تحویل می تواند حداکثر " + patientDrug.getSuggestCount() + " باشد");
        if (patientDrug.isDedicated() && !patientDrug.getGiverId().equals(userId))
            throw new RuntimeException("این تجویز قبلا توسط مسئول داروخانه دیگری تحویل شده است");

        int diff;
        if (!patientDrug.isDedicated()) {
            patientDrug.setDedicated(true);
            patientDrug.setGiveAt(LocalDateTime.now());
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
            ObjectId drugId, LocalDateTime startAdviceAt, LocalDateTime endAdviceAt,
            LocalDateTime startGiveAt, LocalDateTime endGiveAt,
            Integer startSuggestCount, Integer endSuggestCount,
            ObjectId giverId, Integer pageNo
    ) {
        Trip trip;
        List<PatientAdvices> output = new ArrayList<>();

        if (patientId == null) {
            trip = tripRepository.findActiveByAreaIdAndPharmacyManager(
                    areaId, userId, Utility.getCurrLocalDateTime()
            ).orElseThrow(NotAccessException::new);
        } else {
            trip = tripRepository.findActiveByAreaIdAndResponsibleId(
                    areaId, userId, Utility.getCurrLocalDateTime()
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
            ObjectId areaId, ObjectId groupId
    ) {
        PairValue p = checkAccess(userId, areaId);
        Area foundArea = (Area) p.getKey();
        String tripName = p.getValue().toString();
        doReturnAllDrugs(
                foundArea.getName(), tripName, username,
                areaId, userId, groupId
        );
    }

    public void returnDrug(
            ObjectId drugId, int amount,
            ObjectId groupId, ObjectId areaId,
            String username, ObjectId userId
    ) {
        if (userId != null)
            jahadgarDrugService.checkAccessToWareHouse(groupId, userId);

        Trip trip = tripRepository.findByGroupIdAndAreaId(groupId, areaId)
                .orElseThrow(NotAccessException::new);

        trip.getAreas()
                .stream()
                .filter(area -> area.getId().equals(areaId))
                .findFirst().ifPresent(area ->
                        doReturnDrug(
                                area.getName(), trip.getName(),
                                username, areaId, userId,
                                drugId, amount, groupId
                        )
                );
    }

    public void doReturnDrug(
            String areaName, String tripName,
            String username, ObjectId areaId,
            ObjectId userId, ObjectId drugId,
            int amount, ObjectId groupId
    ) {
        AreaDrugs areaDrug = areaDrugsRepository.findByAreaIdAndDrugId(areaId, drugId)
                .orElseThrow(InvalidIdException::new);
        if (areaDrug.getReminder() < amount)
            throw new RuntimeException("تعداد داروهای موجود کمتر از مقدار درخواستی شما می باشد");

        Drug drug = drugRepository.findById(drugId)
                .orElseThrow(InvalidIdException::new);

        final String msg = "عودت " + amount + " تا - از منطقه " + areaName + " در اردو " + tripName + " توسط " + username;
        List<DrugLog> drugLogs = new ArrayList<>();

        drug.setAvailable(drug.getAvailable() + amount);
        areaDrug.setReminder(areaDrug.getReminder() - amount);
        areaDrug.setTotalCount(areaDrug.getTotalCount() - amount);
        areaDrug.setUpdatedAt(LocalDateTime.now());

        drugLogRepository.save(DrugLog
                .builder()
                .drugId(drug.getId())
                .userId(userId)
                .areaId(areaId)
                .groupId(groupId)
                .amount(amount)
                .desc(msg)
                .build());
        drugRepository.save(drug);
        areaDrugsRepository.save(areaDrug);
    }

    public void doReturnAllDrugs(
            String areaName, String tripName,
            String username, ObjectId areaId,
            ObjectId userId, ObjectId groupId
    ) {
        List<AreaDrugs> areaDrugs = areaDrugsRepository.findAvailableDrugsByAreaId(areaId);
        if (areaDrugs.size() == 0)
            return;

        List<Drug> drugs = new ArrayList<>();
        drugRepository.findAllById(areaDrugs.stream()
                .map(AreaDrugs::getDrugId)
                .collect(Collectors.toList())
        ).forEach(drugs::add);

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
                                    .groupId(groupId)
                                    .amount(areaDrugs1.getReminder())
                                    .desc(msg)
                                    .build()
                    );
                    areaDrugs1.setReminder(0);
                    areaDrugs1.setUpdatedAt(LocalDateTime.now());
                }));

        drugLogRepository.saveAll(drugLogs);
        drugRepository.saveAll(drugs);
        areaDrugsRepository.saveAll(areaDrugs);
    }

    public void returnAllDrugsByAdmin(
            ObjectId userId, String username,
            ObjectId areaId, String areaName, String tripName
    ) {
        doReturnAllDrugs(areaName, tripName, username, areaId, userId, null);
    }
}
