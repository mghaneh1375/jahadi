package four.group.jahadi.Models;

import four.group.jahadi.Utility.Utility;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class DrugLogJoinModel {
    private Drug drugInfo;
    private String username;
    private String desc;
    private String group;
    private Integer amount;
    private LocalDateTime created_at;

    public void fillExcelRow(Row row) {
        row.createCell(0).setCellValue(this.drugInfo.getName());
        row.createCell(1).setCellValue(this.drugInfo.getDose());
        row.createCell(2).setCellValue(this.drugInfo.getDrugType().getName());
        row.createCell(3).setCellValue(this.drugInfo.getProducer());
        row.createCell(4).setCellValue(this.drugInfo.getLocation().getFaTranslate());
        row.createCell(5).setCellValue(this.drugInfo.getShelfNo());
        row.createCell(6).setCellValue(this.drugInfo.getBoxNo());
        row.createCell(7).setCellValue(this.amount > 0 ? "ورودی" : "خروجی");
        row.createCell(8).setCellValue(Math.abs(this.amount));
        row.createCell(9).setCellValue(this.group);
        row.createCell(10).setCellValue(this.username);
        row.createCell(11).setCellValue(this.desc);
        row.createCell(12).setCellValue(Utility.convertUTCDateToJalali(this.created_at));
    }
}
