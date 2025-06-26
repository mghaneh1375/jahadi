package four.group.jahadi.Service.Area;

import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Area.ModuleInArea;
import four.group.jahadi.Models.Area.PatientsInArea;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.*;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.GroupQuestion;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.TableQuestion;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Repository.ModuleRepository;
import four.group.jahadi.Repository.PatientRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Repository.UserRepository;
import four.group.jahadi.Service.ExcelService;
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
import java.util.stream.Collectors;

import static four.group.jahadi.Service.Area.AreaUtils.findArea;
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

    public void getModuleReport(
            ObjectId areaId, Module module,
            Sheet sheet
    ) {
        ObjectId moduleId = module.getId();
        HashMap<ObjectId, List<Question>> subModulesQuestions = new HashMap<>();
        AtomicInteger incRowStep = new AtomicInteger(1);

        module.getSubModules()
                .forEach(subModule -> {
                    System.out.println("subModule is " + subModule.getName());
                    System.out.println("questions_size is " + subModule.getQuestions().size());
                    subModulesQuestions.put(subModule.getId(), new ArrayList<>());
                    subModule
                            .getQuestions()
                            .forEach(question -> {
                                System.out.println("---> question type is " + question.getQuestionType());
                                System.out.println("---> question id is " + question.getId());
                                if (question.getQuestionType().equals(QuestionType.TABLE)) {
                                    System.out.println("here1");
                                    return;
                                }
                                if (question.getQuestionType().equals(QuestionType.GROUP)) {
                                    System.out.println("here2");
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
                                    System.out.println("here3");
                                    CheckListGroupQuestion q = (CheckListGroupQuestion) question;
                                    q.getQuestions()
                                            .forEach(question1 -> {
                                                question1.setOptions(q.getOptions());
                                                subModulesQuestions.get(subModule.getId()).add(question1);
                                            });
                                    return;
                                }
                                System.out.println("here4");
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
                                    System.out.println("here5");
                                    ((GroupQuestion) question).getQuestions()
                                            .stream().filter(question1 -> question1.getQuestionType().equals(QuestionType.TABLE))
                                            .forEach(question1 -> {
                                                if (((TableQuestion) question1).getFirstColumn() != null)
                                                    incRowStep.set(Math.max(((TableQuestion) question1).getFirstColumn().size(), incRowStep.get()));
                                                subModulesQuestions.get(subModule.getId()).add(question1);
                                            });
                                    return;
                                }
                                System.out.println("here6");
                                subModulesQuestions.get(subModule.getId()).add(question);
                            });
                });

        List<ObjectId> subModuleIds = module.getSubModules().stream().map(SubModule::getId)
                .collect(Collectors.toList());

        excelService.writeCommonHeader(sheet);
        System.out.println("write header successfully");
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
            System.out.println("**** sub module is **** " + subModule.getName());
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
            System.out.println("write sub module first header successfully");
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
        System.out.println("starting SubModuleSecondHeader");
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
        System.out.println("adding patient row");
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
        Trip wantedTrip = isGroupAccess
                ? tripRepository.findByGroupIdAndAreaId(userId_groupId, areaId).orElseThrow(NotAccessException::new)
                : tripRepository.findByAreaIdAndOwnerId(areaId, userId_groupId).orElseThrow(NotAccessException::new);

        Area area = isGroupAccess
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
            if(wantedModuleId != null && !Objects.equals(wantedModuleId, moduleId))
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
