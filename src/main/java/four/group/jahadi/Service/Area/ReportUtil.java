package four.group.jahadi.Service.Area;

import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Area.PatientAnswer;
import four.group.jahadi.Models.Area.PatientForm;
import four.group.jahadi.Models.Area.PatientsInArea;
import four.group.jahadi.Models.Patient;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.Question.TableQuestion;
import four.group.jahadi.Models.User;
import four.group.jahadi.Utility.Utility;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ReportUtil {

    private static void mergeCell(Sheet sheet, Row r, int cellIdx) {
        sheet.addMergedRegion(new CellRangeAddress(
                r.getRowNum(), r.getRowNum() + 1,
                cellIdx, cellIdx
        ));
    }

    private static Row createNewPatientRow(
            Sheet sheet, Patient wantedPatient,
            int incRowStep
    ) {
        Row r = sheet.createRow(sheet.getLastRowNum() + 1);
        r.createCell(0).setCellValue(wantedPatient.getName());
        r.createCell(1).setCellValue(wantedPatient.getInsurance().getFaTranslate());
        r.createCell(2).setCellValue(wantedPatient.getAgeType().getFaTranslate());
        r.createCell(3).setCellValue(wantedPatient.getSex().getFaTranslate());

        if (incRowStep > 1) {
            for (int i = 0; i < 4; i++)
                mergeCell(sheet, r, i);
        }
        return r;
    }

    private static void addDoctorInfoToPatientRow(
            PatientForm patientForm,
            Row patientRow,
            Optional<User> doc,
            int subModuleStartIdx,
            int incRowStep,
            Sheet sheet,
            String receptedAt
    ) {
        if (patientForm != null && doc.isPresent()) {
            patientRow.createCell(subModuleStartIdx).setCellValue(doc.get().getName());
            patientRow.createCell(subModuleStartIdx + 1).setCellValue(receptedAt);
        } else {
            patientRow.createCell(subModuleStartIdx).setCellValue("");
            patientRow.createCell(subModuleStartIdx + 1).setCellValue("");
        }

        if (incRowStep > 1) {
            mergeCell(sheet, patientRow, subModuleStartIdx);
            mergeCell(sheet, patientRow, subModuleStartIdx + 1);
        }
    }

    static void addPatientRowForSpecificSubModule(
            int subModuleStartIdx,
            List<PatientsInArea> patients,
            List<Patient> patientsInfo,
            Sheet sheet,
            HashMap<ObjectId, Row> patientsRow,
            List<User> doctors,
            final int incRowStep,
            ObjectId moduleId,
            ObjectId subModuleId,
            List<Question> subModuleQuestions,
            HashMap<ObjectId, Integer> questionsColIdx
    ) {
        patients.forEach(patient -> {
//            if (!patient.getId().toString().equals("684148cb75eaf47cef10d302"))
//                return;
            Patient wantedPatient = patientsInfo
                    .stream()
                    .filter(patient1 -> patient1.getId().equals(patient.getPatientId()))
                    .findFirst().get();

            AtomicReference<Row> patientRow = new AtomicReference<>();
            patient.getReferrals()
                    .stream()
                    .filter(patientReferral -> patientReferral.getModuleId().equals(moduleId))
                    .forEach(patientReferral -> {
                        PatientForm patientForm = null;
                        if(patientReferral.getForms() != null) {
                            for (int i = 0; i < patientReferral.getForms().size(); i++) {
                                if (patientReferral.getForms().get(i).getSubModuleId().equals(subModuleId)) {
                                    patientForm = patientReferral.getForms().get(i);
                                    break;
                                }
                            }
                        }
                        if (patientsRow.containsKey(patientReferral.getId()))
                            patientRow.set(patientsRow.get(patientReferral.getId()));
                        else {
                            Row r = createNewPatientRow(sheet, wantedPatient, incRowStep);
                            patientsRow.put(patientReferral.getId(), r);
                            patientRow.set(r);
                        }

                        final int patientRowNum = patientRow.get().getRowNum();
                        PatientForm finalPatientForm = patientForm;

                        addDoctorInfoToPatientRow(
                                patientForm,
                                patientRow.get(),
                                patientForm == null
                                        ? Optional.empty()
                                        : doctors
                                        .stream()
                                        .filter(user -> Objects.equals(finalPatientForm.getDoctorId(), user.getId()))
                                        .findFirst(),
                                subModuleStartIdx,
                                incRowStep, sheet,
                                patientForm == null || patientForm.getCreatedAt() == null
                                        ? ""
                                        : Utility.convertUTCDateToJalali(patientForm.getCreatedAt())
                        );

                        subModuleQuestions.forEach(question -> {
                            if(!questionsColIdx.containsKey(question.getId()))
                                return;

                            Optional<PatientAnswer> patientAnswer1 = finalPatientForm == null
                                    ? Optional.empty()
                                    : finalPatientForm.getAnswers()
                                    .stream()
                                    .filter(patientAnswer -> patientAnswer.getQuestionId().equals(question.getId()))
                                    .findFirst();

                            Cell cell = patientRow.get().createCell(questionsColIdx.get(question.getId()));

                            if (
                                    patientAnswer1.isPresent() &&
                                            (
                                                    question.getQuestionType().equals(QuestionType.SIMPLE) ||
                                                            question.getQuestionType().equals(QuestionType.CHECK_LIST)
                                            ) && ((SimpleQuestion) question).getOptions() != null
                            ) {
                                ((SimpleQuestion) question).getOptions()
                                        .stream()
                                        .filter(pairValue -> pairValue.getKey().equals(
                                                patientAnswer1.get().getAnswer().toString()
                                        )).findFirst().ifPresent(pairValue ->
                                                cell.setCellValue(pairValue.getValue().toString())
                                        );
                            } else if (question.getQuestionType().equals(QuestionType.TABLE)) {
                                TableQuestion tableQuestion = (TableQuestion) question;

                                for (int i = 0; i < tableQuestion.getRowsCount(); i++) {
                                    final Row r;
                                    AtomicReference<Cell> lastCell = new AtomicReference<>(cell);
                                    if (sheet.getRow(patientRowNum + i) == null)
                                        r = sheet.createRow(patientRowNum + i);
                                    else
                                        r = sheet.getRow(patientRowNum + i);

                                    if (tableQuestion.getFirstColumn() != null) {
                                        if(i > 0) {
                                            Cell c = r.createCell(lastCell.get().getColumnIndex());
                                            c.setCellValue(tableQuestion.getFirstColumn().get(i));
                                        }
                                        else
                                            cell.setCellValue(tableQuestion.getFirstColumn().get(i));
                                    }

                                    int finalI = i;
                                    int headerSize = tableQuestion.getFirstColumn() != null
                                            ? tableQuestion.getHeaders().size() - 1
                                            : tableQuestion.getHeaders().size();

                                    patientAnswer1.ifPresent(patientAnswer -> {
                                        Arrays.stream(patientAnswer.getAnswer().toString().split("__"))
                                                .skip((long) finalI * headerSize)
                                                .limit(headerSize)
                                                .forEach(s -> {
                                                    Cell c = r.createCell(lastCell.get().getColumnIndex() + 1);
                                                    c.setCellValue(s);
                                                    lastCell.set(c);
                                                });
                                    });
                                }
                            } else
                                patientAnswer1.ifPresent(patientAnswer -> cell.setCellValue(patientAnswer.getAnswer().toString()));

                            if (incRowStep > 1 && !question.getQuestionType().equals(QuestionType.TABLE))
                                mergeCell(sheet, patientRow.get(), cell.getColumnIndex());
                        });

//                        if (incRowStep > 1)
//                            rowIdx.set(rowIdx.get() + incRowStep - 1);
                    });
        });
    }

}
