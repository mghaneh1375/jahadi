package four.group.jahadi.Models;

import four.group.jahadi.Utility.Utility;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;

@Data
public class DrugJoinModel extends Drug {
    private Group groupInfo;

    public void fillExcelRow(Row row) {
        row.createCell(0).setCellValue(this.groupInfo.getName());
        row.createCell(1).setCellValue(this.getName());
        row.createCell(2).setCellValue(this.getDose());
        row.createCell(3).setCellValue(this.getDrugType().getName());
        row.createCell(4).setCellValue(this.getProducer());
        row.createCell(5).setCellValue(this.getLocation().getFaTranslate());
        row.createCell(6).setCellValue(this.getPrice());
        row.createCell(7).setCellValue(this.getShelfNo());
        row.createCell(8).setCellValue(this.getBoxNo());
        row.createCell(9).setCellValue(Utility.convertUTCDateToJalali(this.getCreatedAt()));
        row.createCell(10).setCellValue(this.getAvailable());
        row.createCell(11).setCellValue(this.getAvailablePack());
        row.createCell(12).setCellValue(this.getPrice());
        row.createCell(13).setCellValue(this.getDescription());
    }
}
