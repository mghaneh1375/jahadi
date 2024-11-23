package four.group.jahadi.Service;

import four.group.jahadi.Models.Question.*;
import four.group.jahadi.Models.SubModule;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExcelService {

    public Workbook createExcel(List<String> sheets) {
        Workbook workbook = new XSSFWorkbook();
        sheets.forEach(workbook::createSheet);
        return workbook;
    }

    private CellRangeAddress isMergedCell(int row, int column, Sheet sheet) {
        for (CellRangeAddress range : sheet.getMergedRegions()) {
            if (range.isInRange(row, column)) {
                return range;
            }
        }

        return null;
    }

    public void writeHeader(Sheet sheet, SubModule subModule) {
        int rowIdx = 0;
        Row row = sheet.createRow(rowIdx);
        row.createCell(0).setCellValue("نام بیمار");
        row.createCell(1).setCellValue("بیمه");
        row.createCell(2).setCellValue("سن");
        row.createCell(3).setCellValue("جنسیت");
        row.createCell(4).setCellValue("نام دکتر");
        row.createCell(5).setCellValue("زمان پذیرش");
        AtomicInteger colIdx = new AtomicInteger(6);
        AtomicInteger maxRow = new AtomicInteger(1);
        HashMap<Integer, Question> complexQuestions = new HashMap<>();

        subModule.getQuestions().forEach(question -> {
            switch (question.getQuestionType()) {
                case SIMPLE:
                    row.createCell(colIdx.getAndIncrement())
                            .setCellValue(((SimpleQuestion) question).getQuestion());
                    break;
                case CHECK_LIST:
                    CheckListGroupQuestion checkListGroupQuestion = (CheckListGroupQuestion) question;
                    row.createCell(colIdx.get())
                            .setCellValue(checkListGroupQuestion.getSectionTitle());
                    sheet.addMergedRegion(
                            new CellRangeAddress(
                                    0, 0, colIdx.get(), colIdx.get() + checkListGroupQuestion.getQuestions().size() - 1
                            )
                    );
                    complexQuestions.put(colIdx.get(), question);
                    colIdx.set(colIdx.get() + checkListGroupQuestion.getQuestions().size());
                    maxRow.set(2);
                    break;
                case TABLE:
                    complexQuestions.put(colIdx.get(), question);
                    maxRow.set(2);
                    break;
                case GROUP:
                    ((GroupQuestion) question).getQuestions().forEach(question1 -> {
                        switch (question1.getQuestionType()) {
                            case SIMPLE:
                                row.createCell(colIdx.getAndIncrement())
                                        .setCellValue(((SimpleQuestion) question1).getQuestion());
                                break;
                            case CHECK_LIST:
                                ((CheckListGroupQuestion) question1).getQuestions().forEach(question11 ->
                                        row.createCell(colIdx.getAndIncrement()).setCellValue(question11.getQuestion())
                                );
                                complexQuestions.put(colIdx.get(), question1);
                                maxRow.set(2);
                                break;
                            case TABLE:
                                complexQuestions.put(colIdx.get(), question1);
                                row.createCell(colIdx.get())
                                        .setCellValue(
                                                ((TableQuestion) question1).getTitle() == null ? "test" :
                                                        ((TableQuestion) question1).getTitle()
                                        );
                                sheet.addMergedRegion(
                                        new CellRangeAddress(
                                                0, 0,
                                                colIdx.get(),
                                                ((TableQuestion) question1).getFirstColumn() == null
                                                        ? colIdx.get() + ((TableQuestion) question1).getHeaders().size() - 1
                                                        : colIdx.get() + ((TableQuestion) question1).getHeaders().size()
                                        )
                                );
                                colIdx.set(colIdx.get() + ((TableQuestion) question1).getHeaders().size() + 1);
                                maxRow.set(2);
                                break;
                        }
                    });
                    break;
            }
        });

        if (maxRow.get() > 1) {
            for (int i = 1; i < maxRow.get(); i++)
                sheet.createRow(i);
            for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
                CellRangeAddress mergedCell = isMergedCell(0, i, sheet);
                if (mergedCell == null) {
                    sheet.addMergedRegion(
                            new CellRangeAddress(
                                    0, maxRow.get() - 1,
                                    i, i
                            )
                    );
                }
            }
        }

        complexQuestions.keySet().forEach(colIndex -> {
            Question question = complexQuestions.get(colIndex);
            switch (question.getQuestionType()) {
                case CHECK_LIST:
                    AtomicInteger counter = new AtomicInteger(0);
                    ((CheckListGroupQuestion) question).getQuestions().forEach(question1 ->
                            sheet.getRow(1).createCell(colIndex + counter.getAndIncrement()).setCellValue(question1.getQuestion())
                    );
                    break;
                case TABLE:
                    AtomicInteger counter2 = new AtomicInteger(0);
                    TableQuestion tableQuestion1 = ((TableQuestion) question);
                    Row newRow = sheet.getRow(1);
                    if(tableQuestion1.getFirstColumn() != null)
                        newRow.createCell(colIndex + counter2.getAndIncrement());

                    tableQuestion1.getHeaders().forEach(s -> newRow
                            .createCell(colIndex + counter2.getAndIncrement())
                            .setCellValue(s));
                    break;
            }
        });
    }

}
