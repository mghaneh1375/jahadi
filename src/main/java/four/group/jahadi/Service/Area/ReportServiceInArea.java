package four.group.jahadi.Service.Area;

import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.*;
import four.group.jahadi.Models.Area.*;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.*;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Repository.ModuleRepository;
import four.group.jahadi.Repository.PatientRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Repository.UserRepository;
import four.group.jahadi.Service.ExcelService;
import four.group.jahadi.Utility.Utility;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static four.group.jahadi.Service.Area.AreaUtils.findArea;
import static four.group.jahadi.Service.Area.AreaUtils.findModule;
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
    private ExcelService excelService;
    @Autowired
    private PatientRepository patientRepository;

    public void moduleReport(
            ObjectId userId, ObjectId areaId,
            ObjectId moduleId, HttpServletResponse response
    ) {
        Trip wantedTrip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area area = findArea(wantedTrip, areaId, userId);
        findModule(area, moduleId, null);
        Module module = moduleRepository.findById(moduleId).get();
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
                                if (question.getQuestionType().equals(QuestionType.CHECK_LIST)) {
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

        Workbook workbook = excelService.createExcel(
                module.getSubModules().stream().map(SubModule::getName)
                        .collect(Collectors.toList())
        );
        List<ObjectId> subModuleIds = module.getSubModules().stream().map(SubModule::getId)
                .collect(Collectors.toList());

        List<Short> maxRows = new ArrayList<>();
        AtomicInteger idx = new AtomicInteger();
        module.getSubModules().forEach(subModule -> {
                    Sheet sheet = workbook.getSheetAt(idx.getAndIncrement());
                    maxRows.add(excelService.writeHeader(sheet, subModule));
                    for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
                        CellRangeAddress mergedCell = isMergedCell(0, i, sheet);
                        if (mergedCell == null)
                            sheet.setColumnWidth(i, 18 * 255);
                        else {
                            mergedCell.forEach(cellAddress -> sheet.setColumnWidth(cellAddress.getColumn(), 18 * 255));
                        }
                    }
                }
        );

        List<PatientsInArea> patients = patientsInAreaRepository.findByAreaIdAndModuleId(areaId, moduleId);
        if (patients.size() > 0) {
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

            AtomicInteger sheetIdx = new AtomicInteger(0);
            AtomicInteger rowIdx = new AtomicInteger();
            subModuleIds.forEach(subModuleId -> {
                Sheet sheet = workbook.getSheetAt(sheetIdx.get());
                rowIdx.set(maxRows.get(sheetIdx.get()));
                patients.forEach(patient -> {
                    Patient wantedPatient = patientsInfo
                            .stream()
                            .filter(patient1 -> patient1.getId().equals(patient.getPatientId()))
                            .findFirst().get();
                    patient.getReferrals()
                            .stream()
                            .filter(patientReferral -> patientReferral.getModuleId().equals(moduleId))
                            .forEach(patientReferral -> {
                                patientReferral.getForms()
                                        .stream()
                                        .filter(patientForm -> patientForm.getSubModuleId().equals(subModuleId))
                                        .forEach(patientForm -> {
                                            Row row = sheet.createRow(rowIdx.getAndIncrement());
                                            row.createCell(0).setCellValue(wantedPatient.getName());
                                            row.createCell(1).setCellValue(wantedPatient.getInsurance().getFaTranslate());
                                            row.createCell(2).setCellValue(wantedPatient.getAgeType().getFaTranslate());
                                            row.createCell(3).setCellValue(wantedPatient.getSex().getFaTranslate());
                                            User doctor = doctors
                                                    .stream()
                                                    .filter(user -> user.getId().equals(patientForm.getDoctorId()))
                                                    .findFirst().get();
                                            row.createCell(4).setCellValue(doctor.getName());
                                            row.createCell(5).setCellValue(patientReferral.getReceptedAt() == null ? "" : Utility.convertUTCDateToJalali(patientReferral.getReceptedAt()));

                                            if (incRowStep.get() > 1) {
                                                for (int i = 0; i < 6; i++) {
                                                    sheet.addMergedRegion(new CellRangeAddress(
                                                            rowIdx.get() - 1, rowIdx.get(),
                                                            i, i
                                                    ));
                                                }
                                            }

                                            AtomicInteger cellIdx = new AtomicInteger(6);
                                            subModulesQuestions.get(subModuleId).forEach(question -> {
                                                Optional<PatientAnswer> patientAnswer1 = patientForm.getAnswers()
                                                        .stream()
                                                        .filter(patientAnswer -> patientAnswer.getQuestionId().equals(question.getId()))
                                                        .findFirst();
                                                if (patientAnswer1.isEmpty()) {
                                                    row.createCell(cellIdx.getAndIncrement());
                                                    if (incRowStep.get() > 1) {
                                                        sheet.addMergedRegion(new CellRangeAddress(
                                                                rowIdx.get() - 1, rowIdx.get(),
                                                                cellIdx.get() - 1, cellIdx.get() - 1
                                                        ));
                                                    }
                                                } else {
                                                    if (question.getQuestionType().equals(QuestionType.SIMPLE) &&
                                                            ((SimpleQuestion) question).getOptions() != null
                                                    ) {
                                                        Cell cell = row.createCell(cellIdx.getAndIncrement());
                                                        ((SimpleQuestion) question).getOptions()
                                                                .stream()
                                                                .filter(pairValue -> pairValue.getKey().equals(
                                                                        patientAnswer1.get().getAnswer().toString()
                                                                )).findFirst().ifPresent(pairValue ->
                                                                        cell.setCellValue(pairValue.getValue().toString())
                                                                );
                                                    } else if (question.getQuestionType().equals(QuestionType.TABLE)) {
                                                        TableQuestion tableQuestion = (TableQuestion) question;
                                                        Row r;
                                                        AtomicInteger cellIdxTable = new AtomicInteger();
                                                        AtomicInteger counter = new AtomicInteger(-1);
                                                        for (int i = 0; i < tableQuestion.getRowsCount(); i++) {
                                                            if (sheet.getRow(rowIdx.get() + counter.get()) == null)
                                                                r = sheet.createRow(rowIdx.get() + counter.getAndIncrement());
                                                            else
                                                                r = sheet.getRow(rowIdx.get() + counter.getAndIncrement());
                                                            Row finalR = r;
                                                            cellIdxTable.set(cellIdx.get());
                                                            if (tableQuestion.getFirstColumn() != null)
                                                                finalR.createCell(cellIdxTable.getAndIncrement()).setCellValue(tableQuestion.getFirstColumn().get(i));
                                                            Arrays.stream(patientAnswer1.get().getAnswer().toString().split("__")).skip((long) i * tableQuestion.getHeaders().size()).limit(tableQuestion.getHeaders().size()).forEach(s -> {
                                                                        finalR.createCell(cellIdxTable.getAndIncrement()).setCellValue(s);
                                                                    }
                                                            );
                                                        }
                                                        cellIdx.set(cellIdx.get() + tableQuestion.getHeaders().size());
                                                    } else
                                                        row.createCell(cellIdx.getAndIncrement()).setCellValue(patientAnswer1.get().getAnswer().toString());

                                                    if (incRowStep.get() > 1 && !question.getQuestionType().equals(QuestionType.TABLE)) {
                                                        sheet.addMergedRegion(new CellRangeAddress(
                                                                rowIdx.get() - 1, rowIdx.get(),
                                                                cellIdx.get() - 1, cellIdx.get() - 1
                                                        ));
                                                    }
                                                }
                                            });

                                            if (incRowStep.get() > 1)
                                                rowIdx.set(rowIdx.get() + incRowStep.get() - 1);
                                        });
                            });
                });
                sheetIdx.getAndIncrement();
            });
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.builder("attachment").filename("module.xlsx").build().toString()
        );
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            response.setStatus(HttpStatus.OK.value());
            response.flushBuffer();
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
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
//            if(!subModule.getId().toString().equals("6841f4ce9f06e166a2210f36") &&
//                    !subModule.getId().toString().equals("6841f4ce9f06e166a2210f43") &&
//                    !subModule.getId().toString().equals("6841f4ce9f06e166a2210f56") &&
//                    !subModule.getId().toString().equals("6841f4ce9f06e166a2210f66"))
//                return;
//            if (!subModule.getId().toString().equals("6841f4ce9f06e166a2210f05") &&
//                    !subModule.getId().toString().equals("6841f4ce9f06e166a2210f11")
//            )
//                return;

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
            ObjectId userId, ObjectId areaId,
            HttpServletResponse response
    ) {
        Trip wantedTrip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);
        Area area = findArea(wantedTrip, areaId, userId);

        Workbook workbook = excelService.createExcel(
                area.getModules().stream().map(module -> module.getModuleName().replace("/", " "))
                        .collect(Collectors.toList())
        );

        for (int z = 0; z < area.getModules().size(); z++) {
            ModuleInArea areaModule = area.getModules().get(z);
            ObjectId moduleId = areaModule.getModuleId();
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

        response.setContentType("application/vnd.ms-excel");
        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.builder("attachment").filename("module.xlsx").build().toString()
        );
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            response.setStatus(HttpStatus.OK.value());
            response.flushBuffer();
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
