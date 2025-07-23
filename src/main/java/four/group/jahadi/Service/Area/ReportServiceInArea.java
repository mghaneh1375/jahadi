package four.group.jahadi.Service.Area;

import four.group.jahadi.DTO.Area.PatientDrugJoinDto;
import four.group.jahadi.Enums.Module.DeliveryStatus;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.*;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.*;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.GroupQuestion;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.TableQuestion;
import four.group.jahadi.Repository.Area.PatientsDrugRepository;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Repository.ModuleRepository;
import four.group.jahadi.Repository.PatientRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Repository.UserRepository;
import four.group.jahadi.Service.ExcelService;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static four.group.jahadi.Service.Area.AreaUtils.findArea;
import static four.group.jahadi.Service.Area.ReportUtil.prepareHttpServletResponse;
import static four.group.jahadi.Service.ExcelService.isMergedCell;

@Service
public class ReportServiceInArea {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PatientsInAreaRepository patientsInAreaRepository;
    @Autowired
    private PatientsDrugRepository patientsDrugRepository;
    @Autowired
    private ExcelService excelService;
    @Autowired
    private PatientRepository patientRepository;

    public void getPatientReport(
            Patient patient, Module module, Sheet sheet,
            HashMap<ObjectId, List<PatientForm>> patientForms
    ) {
        ObjectId moduleId = module.getId();
        HashMap<ObjectId, List<Question>> subModulesQuestions = new HashMap<>();
        AtomicInteger incRowStep = new AtomicInteger(1);

        module.getSubModules()
                .forEach(subModule -> {
                    subModulesQuestions.put(subModule.getId(), new ArrayList<>());
                    subModule
                            .getQuestions()
                            .forEach(question -> {
                                if (question.getQuestionType().equals(QuestionType.TABLE))
                                    return;
                                if (question.getQuestionType().equals(QuestionType.GROUP)) {
                                    ((GroupQuestion) question).getQuestions()
                                            .stream().filter(question1 -> !question1.getQuestionType().equals(QuestionType.TABLE))
                                            .forEach(question1 -> {
                                                subModulesQuestions.get(subModule.getId()).add(question1);
                                            });
                                    return;
                                }
                                if (question.getQuestionType().equals(QuestionType.CHECK_LIST) &&
                                        question instanceof CheckListGroupQuestion
                                ) {
                                    CheckListGroupQuestion q = (CheckListGroupQuestion) question;
                                    q.getQuestions()
                                            .forEach(question1 -> {
                                                question1.setOptions(q.getOptions());
                                                subModulesQuestions.get(subModule.getId()).add(question1);
                                            });
                                    return;
                                }
                                subModulesQuestions.get(subModule.getId()).add(question);
                            });
                    subModule
                            .getQuestions()
                            .forEach(question -> {
                                if (!question.getQuestionType().equals(QuestionType.TABLE) &&
                                        !question.getQuestionType().equals(QuestionType.GROUP)
                                )
                                    return;
                                if (question.getQuestionType().equals(QuestionType.GROUP)) {
                                    ((GroupQuestion) question).getQuestions()
                                            .stream().filter(question1 -> question1.getQuestionType().equals(QuestionType.TABLE))
                                            .forEach(question1 -> {
                                                if (((TableQuestion) question1).getFirstColumn() != null)
                                                    incRowStep.set(Math.max(((TableQuestion) question1).getFirstColumn().size(), incRowStep.get()));
                                                subModulesQuestions.get(subModule.getId()).add(question1);
                                            });
                                    return;
                                }
                                subModulesQuestions.get(subModule.getId()).add(question);
                            });
                });

        List<ObjectId> subModuleIds = module.getSubModules()
                .stream()
                .map(SubModule::getId)
                .collect(Collectors.toList());

        excelService.writeCommonHeader2(sheet);
        Row row = sheet.getRow(0);
        Workbook wb = row.getSheet().getWorkbook();

        CellStyle parentCellStyle = wb.createCellStyle();
        parentCellStyle.setAlignment(HorizontalAlignment.CENTER);
        Font font = wb.createFont();
        font.setFontHeight((short) 300);
        font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        font.setBold(true);
        parentCellStyle.setFont(font);

        int startIdx = 4;
        int maxRowIdx = 1;
        HashMap<ObjectId, HashMap<ObjectId, Integer>> questionsColIdx = new HashMap<>();
        HashMap<ObjectId, Integer> startIndicesHistory = new HashMap<>();
        for (int z = 0; z < module.getSubModules().size(); z++) {
            SubModule subModule = module.getSubModules().get(z);
            startIndicesHistory.put(subModule.getId(), startIdx);
            questionsColIdx.put(subModule.getId(), new HashMap<>());

            short[] output = excelService.writeSubModuleFirstHeader(
                    sheet, parentCellStyle, subModule, startIdx, maxRowIdx,
                    questionsColIdx.get(subModule.getId())
            );
            maxRowIdx = output[0];
            short lastCellNum = output[1];
            for (int i = startIdx; i < lastCellNum; i++) {
                CellRangeAddress mergedCell = isMergedCell(1, i, sheet);
                if (mergedCell == null)
                    sheet.setColumnWidth(i, 18 * 255);
                else
                    mergedCell.forEach(cellAddress -> sheet.setColumnWidth(cellAddress.getColumn(), 18 * 255));
            }
            startIdx = lastCellNum;
        }

        if (maxRowIdx > 1) {
            for (int i = 2; i <= maxRowIdx; i++)
                sheet.createRow(i);
            for (int i = 4; i < sheet.getRow(1).getLastCellNum(); i++) {
                CellRangeAddress mergedCell = isMergedCell(1, i, sheet);
                if (mergedCell == null) {
                    sheet.addMergedRegion(
                            new CellRangeAddress(
                                    1, maxRowIdx,
                                    i, i
                            )
                    );
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            sheet.addMergedRegion(
                    new CellRangeAddress(
                            0, maxRowIdx, i, i
                    )
            );
        }
        for (int z = 0; z < module.getSubModules().size(); z++) {
            SubModule subModule = module.getSubModules().get(z);
            excelService.writeSubModuleSecondHeader(
                    sheet, subModule, startIndicesHistory.get(subModule.getId())
            );
        }

        if(patientForms == null) return;
        Set<ObjectId> doctorIds = new HashSet<>();
        if(patientForms != null) {
            for (ObjectId oId : patientForms.keySet()) {
                if (patientForms.get(oId) == null)
                    continue;
                patientForms.get(oId).forEach(patientForm -> {
                    if (patientForm != null)
                        doctorIds.add(patientForm.getDoctorId());
                });
            }
        }

        List<User> doctors = userRepository.findJustNameByIdsIn(new ArrayList<>(doctorIds));
        HashMap<ObjectId, Row> patientsRow = new HashMap<>();
        subModuleIds.forEach(subModuleId -> {
            for(ObjectId referId : patientForms.keySet()) {
                ReportUtil.addPatientRowForSpecificSubModule(
                        startIndicesHistory.get(subModuleId),
                        patient,
                        patientForms.get(referId),
                        sheet,
                        patientsRow,
                        doctors,
                        incRowStep.get(),
                        referId,
                        subModuleId,
                        subModulesQuestions.get(subModuleId),
                        questionsColIdx.get(subModuleId)
                );
            }
        });
    }

    public void getModuleReport(
            ObjectId areaId, Module module,
            Sheet sheet
    ) {
        ObjectId moduleId = module.getId();
        HashMap<ObjectId, List<Question>> subModulesQuestions = new HashMap<>();
        AtomicInteger incRowStep = new AtomicInteger(1);

        module.getSubModules()
                .forEach(subModule -> {
                    subModulesQuestions.put(subModule.getId(), new ArrayList<>());
                    subModule
                            .getQuestions()
                            .forEach(question -> {
                                if (question.getQuestionType().equals(QuestionType.TABLE))
                                    return;
                                if (question.getQuestionType().equals(QuestionType.GROUP)) {
                                    ((GroupQuestion) question).getQuestions()
                                            .stream().filter(question1 -> !question1.getQuestionType().equals(QuestionType.TABLE))
                                            .forEach(question1 -> {
                                                subModulesQuestions.get(subModule.getId()).add(question1);
                                            });
                                    return;
                                }
                                if (question.getQuestionType().equals(QuestionType.CHECK_LIST) &&
                                        question instanceof CheckListGroupQuestion
                                ) {
                                    CheckListGroupQuestion q = (CheckListGroupQuestion) question;
                                    q.getQuestions()
                                            .forEach(question1 -> {
                                                question1.setOptions(q.getOptions());
                                                subModulesQuestions.get(subModule.getId()).add(question1);
                                            });
                                    return;
                                }
                                subModulesQuestions.get(subModule.getId()).add(question);
                            });
                    subModule
                            .getQuestions()
                            .forEach(question -> {
                                if (!question.getQuestionType().equals(QuestionType.TABLE) &&
                                        !question.getQuestionType().equals(QuestionType.GROUP)
                                )
                                    return;
                                if (question.getQuestionType().equals(QuestionType.GROUP)) {
                                    ((GroupQuestion) question).getQuestions()
                                            .stream().filter(question1 -> question1.getQuestionType().equals(QuestionType.TABLE))
                                            .forEach(question1 -> {
                                                if (((TableQuestion) question1).getFirstColumn() != null)
                                                    incRowStep.set(Math.max(((TableQuestion) question1).getFirstColumn().size(), incRowStep.get()));
                                                subModulesQuestions.get(subModule.getId()).add(question1);
                                            });
                                    return;
                                }
                                subModulesQuestions.get(subModule.getId()).add(question);
                            });
                });

        List<ObjectId> subModuleIds = module.getSubModules().stream().map(SubModule::getId)
                .collect(Collectors.toList());

        excelService.writeCommonHeader(sheet);
        Row row = sheet.getRow(0);
        Workbook wb = row.getSheet().getWorkbook();

        CellStyle parentCellStyle = wb.createCellStyle();
        parentCellStyle.setAlignment(HorizontalAlignment.CENTER);
        Font font = wb.createFont();
        font.setFontHeight((short) 300);
        font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        font.setBold(true);
        parentCellStyle.setFont(font);

        int startIdx = 4;
        int maxRowIdx = 1;
        HashMap<ObjectId, HashMap<ObjectId, Integer>> questionsColIdx = new HashMap<>();
        HashMap<ObjectId, Integer> startIndicesHistory = new HashMap<>();
        for (int z = 0; z < module.getSubModules().size(); z++) {
            SubModule subModule = module.getSubModules().get(z);
            startIndicesHistory.put(subModule.getId(), startIdx);
            questionsColIdx.put(subModule.getId(), new HashMap<>());

            short[] output = excelService.writeSubModuleFirstHeader(
                    sheet, parentCellStyle, subModule, startIdx, maxRowIdx,
                    questionsColIdx.get(subModule.getId())
            );
            maxRowIdx = output[0];
            short lastCellNum = output[1];
            for (int i = startIdx; i < lastCellNum; i++) {
                CellRangeAddress mergedCell = isMergedCell(1, i, sheet);
                if (mergedCell == null)
                    sheet.setColumnWidth(i, 18 * 255);
                else
                    mergedCell.forEach(cellAddress -> sheet.setColumnWidth(cellAddress.getColumn(), 18 * 255));
            }
            startIdx = lastCellNum;
        }

        if (maxRowIdx > 1) {
            for (int i = 2; i <= maxRowIdx; i++)
                sheet.createRow(i);
            for (int i = 4; i < sheet.getRow(1).getLastCellNum(); i++) {
                CellRangeAddress mergedCell = isMergedCell(1, i, sheet);
                if (mergedCell == null) {
                    sheet.addMergedRegion(
                            new CellRangeAddress(
                                    1, maxRowIdx,
                                    i, i
                            )
                    );
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            sheet.addMergedRegion(
                    new CellRangeAddress(
                            0, maxRowIdx, i, i
                    )
            );
        }
        for (int z = 0; z < module.getSubModules().size(); z++) {
            SubModule subModule = module.getSubModules().get(z);
            excelService.writeSubModuleSecondHeader(
                    sheet, subModule, startIndicesHistory.get(subModule.getId())
            );
        }

        List<PatientsInArea> patients = patientsInAreaRepository.findByAreaIdAndModuleId(areaId, moduleId);
        if (patients.size() == 0)
            return;

        Set<ObjectId> patientIds = new HashSet<>();
        Set<ObjectId> doctorIds = new HashSet<>();
        patients.forEach(patientsInArea -> {
            patientIds.add(patientsInArea.getPatientId());
            patientsInArea.getReferrals().forEach(patientReferral -> {
                if (patientReferral.getForms() == null)
                    return;
                patientReferral
                        .getForms()
                        .forEach(patientForm -> doctorIds.add(patientForm.getDoctorId()));
            });
        });

        List<Patient> patientsInfo = patientRepository.findExcelInfoByIdIn(new ArrayList<>(patientIds));
        List<User> doctors = userRepository.findJustNameByIdsIn(new ArrayList<>(doctorIds));

        HashMap<ObjectId, Row> patientsRow = new HashMap<>();
        subModuleIds.forEach(subModuleId -> {
//            if (
//                !subModuleId.toString().equals("6841f4ce9f06e166a2210f36") &&
//                !subModuleId.toString().equals("6841f4ce9f06e166a2210f43") &&
//                !subModuleId.toString().equals("6841f4ce9f06e166a2210f56") &&
//                !subModuleId.toString().equals("6841f4ce9f06e166a2210f66")
////                    !subModuleId.toString().equals("6841f4ce9f06e166a2210f05") &&
////                    !subModuleId.toString().equals("6841f4ce9f06e166a2210f11")
//            )
//                return;

            ReportUtil.addPatientRowForSpecificSubModule(
                    startIndicesHistory.get(subModuleId),
                    patients,
                    patientsInfo,
                    sheet,
                    patientsRow,
                    doctors,
                    incRowStep.get(),
                    moduleId,
                    subModuleId,
                    subModulesQuestions.get(subModuleId),
                    questionsColIdx.get(subModuleId)
            );
        });
    }

    public void getAreaReport(
            final ObjectId userId_groupId, final boolean isGroupAccess,
            final ObjectId areaId, final ObjectId wantedModuleId,
            HttpServletResponse response
    ) {
        Trip wantedTrip = userId_groupId == null ?
                tripRepository.findByAreaId(areaId).orElseThrow(InvalidIdException::new)
                : isGroupAccess
                ? tripRepository.findByGroupIdAndAreaId(userId_groupId, areaId).orElseThrow(NotAccessException::new)
                : tripRepository.findByAreaIdAndOwnerId(areaId, userId_groupId).orElseThrow(NotAccessException::new);

        Area area = isGroupAccess || userId_groupId == null
                ? findArea(wantedTrip, areaId)
                : findArea(wantedTrip, areaId, userId_groupId);

        Workbook workbook = excelService.createExcel(
                (
                        wantedModuleId == null
                                ? area.getModules().stream()
                                : area.getModules().stream().filter(module -> Objects.equals(module.getModuleId(), wantedModuleId))
                ).map(module -> module.getModuleName().replace("/", " ")).collect(Collectors.toList())
        );

        for (int z = 0; z < area.getModules().size(); z++) {
            ModuleInArea areaModule = area.getModules().get(z);
            ObjectId moduleId = areaModule.getModuleId();
            if (wantedModuleId != null && !Objects.equals(wantedModuleId, moduleId))
                continue;

            moduleRepository.findById(moduleId).ifPresent(module -> {
                Sheet sheet = null;
                for (int i = 0; i < area.getModules().size(); i++) {
                    if (workbook.getSheetAt(i).getSheetName().equals(areaModule.getModuleName().replace("/", " "))) {
                        sheet = workbook.getSheetAt(i);
                        break;
                    }
                }
                if (sheet != null)
                    getModuleReport(areaId, module, sheet);
            });
        }

        prepareHttpServletResponse(response, workbook, "moduleReport");
    }

    private final static List<String> patientDrugsReportHeaders =
            new ArrayList<>() {
                {
                    add("نام بیمار");
                    add("کدملی/اتباع بیمار");
                    add("نام دارو تجویز شده");
                    add("تعداد دارو تجویز شده");
                    add("نوع دارو تجویز شده");
                    add("دُز دارو تجویز شده");
                    add("شرکت سازنده دارو تجویز شده");
                    add("زمان مصرف دارو تجویز شده");
                    add("مقدار مصرف دارو تجویز شده");
                    add("نحوه مصرف دارو تجویز شده");
                    add("توضیحات تجویز");
                    add("دکتر تجویز کننده");
                    add("زمان تجویز");
                    add("نام دارو تحویل شده");
                    add("تعداد دارو تحویل شده");
                    add("نام تحویل دهنده");
                    add("زمان تحویل");
                    add("توضیح تحویل");
                }
            };

    private final static List<String> drugsReportHeaders =
            new ArrayList<>() {
                {
                    add("نام دارو");
                    add("تعداد کل موجود در اردو");
                    add("تعداد تجویز شده");
                    add("تعداد تحویل شده به بیمار");
                    add("تعداد باقی مانده");
                }
            };

    public void getPatientDrugReport(
            final ObjectId userId_groupId, final boolean isGroupAccess,
            final ObjectId areaId, ObjectId doctorId,
            DeliveryStatus deliveryStatus, ObjectId drugId,
            LocalDateTime startAdviceAt, LocalDateTime endAdviceAt,
            LocalDateTime startGiveAt, LocalDateTime endGiveAt,
            ObjectId giverId,
            HttpServletResponse response
    ) {
        Trip wantedTrip = userId_groupId == null ?
                tripRepository.findByAreaId(areaId).orElseThrow(InvalidIdException::new)
                : isGroupAccess
                ? tripRepository.findByGroupIdAndAreaId(userId_groupId, areaId).orElseThrow(NotAccessException::new)
                : tripRepository.findByAreaIdAndOwnerId(areaId, userId_groupId).orElseThrow(NotAccessException::new);

        if(userId_groupId != null) {
            if(isGroupAccess) findArea(wantedTrip, areaId);
            else findArea(wantedTrip, areaId, userId_groupId);
        }

        List<PatientDrugJoinDto> patientsDrugs =
                patientsDrugRepository.findByFiltersJoinWithDrugAndPatient(
                                areaId, doctorId,
                                deliveryStatus == null ? null : Objects.equals(DeliveryStatus.DELIVERED, deliveryStatus),
                                drugId,
                                startAdviceAt, endAdviceAt,
                                startGiveAt, endGiveAt,
                                giverId, 0, Integer.MAX_VALUE
                        )
                        .stream()
                        .map(PatientDrugJoinDto::mapModelToDto)
                        .collect(Collectors.toList());

        Workbook workbook = ReportUtil.createWorkbook(patientDrugsReportHeaders);

        AtomicInteger counter = new AtomicInteger(1);
        Sheet sheet = workbook.getSheetAt(0);
        patientsDrugs.forEach(patientDrugJoinDto -> {
            Row r = sheet.createRow(counter.getAndIncrement());
            patientDrugJoinDto.fillRowExcelFromDto(r);
        });

        prepareHttpServletResponse(response, workbook, "patientDrugReport");
    }

    public void getAreaDrugReport(
            final ObjectId userId_groupId,
            final boolean isGroupAccess,
            final ObjectId areaId,
            HttpServletResponse response
    ) {
        Trip wantedTrip = userId_groupId == null ?
                tripRepository.findByAreaId(areaId).orElseThrow(InvalidIdException::new)
                : isGroupAccess
                ? tripRepository.findByGroupIdAndAreaId(userId_groupId, areaId).orElseThrow(NotAccessException::new)
                : tripRepository.findByAreaIdAndOwnerId(areaId, userId_groupId).orElseThrow(NotAccessException::new);

        if(userId_groupId != null) {
            if(isGroupAccess) findArea(wantedTrip, areaId);
            else findArea(wantedTrip, areaId, userId_groupId);
        }

        List<DrugAggregationModel> info = patientsDrugRepository.findSumOfSuggestPerDrug(areaId);
        Workbook workbook = ReportUtil.createWorkbook(drugsReportHeaders);

        AtomicInteger counter = new AtomicInteger(1);
        Sheet sheet = workbook.getSheetAt(0);
        info.forEach(item -> {
            Row r = sheet.createRow(counter.getAndIncrement());
            r.createCell(0).setCellValue(item.getName());
            r.createCell(1).setCellValue(item.getTotalCount());
            r.createCell(2).setCellValue(item.getSumSuggestCount());
            r.createCell(3).setCellValue(item.getSumGiveCount());
            r.createCell(4).setCellValue(item.getReminder());
        });

        prepareHttpServletResponse(response, workbook, "areaDrugReport");
    }
}
