package four.group.jahadi.Models;

import four.group.jahadi.Utility.Utility;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;

@Data
public class DrugJoinModel extends Drug {
    private Group groupInfo;

    public void fillExcelRow(Row row) {
        int i = 0;
        row.createCell(i++).setCellValue(this.groupInfo.getName());
        row.createCell(i++).setCellValue(this.getName());
        row.createCell(i++).setCellValue(this.getCode());
        row.createCell(i++).setCellValue(this.getDose());
        row.createCell(i++).setCellValue(this.getDrugType().getName());
        row.createCell(i++).setCellValue(this.getProducer());
        row.createCell(i++).setCellValue(this.getLocation().getFaTranslate());
        row.createCell(i++).setCellValue(this.getPrice());
        row.createCell(i++).setCellValue(this.getShelfNo());
        row.createCell(i++).setCellValue(this.getBoxNo());
        row.createCell(i++).setCellValue(Utility.convertUTCDateToJalali(this.getCreatedAt()));
        row.createCell(i++).setCellValue(this.getAvailable());
        row.createCell(i++).setCellValue(this.getAvailablePack());
        row.createCell(i++).setCellValue(this.getPrice());
        row.createCell(i++).setCellValue(this.getDescription());
    }
}
