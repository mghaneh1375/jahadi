package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.DateSerialization;
import four.group.jahadi.Models.ObjectIdSerialization;
import four.group.jahadi.Utility.Utility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
@Builder
public class PatientExternalForm {
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId formId;
    @JsonSerialize(using = DateSerialization.class)
    private LocalDateTime statusLastModifiedAt;
    private String referredFrom;
    private String referredTo;
    private String reason;
    private String createdAt;
    private String doctor;
    private String status;

    public void fillExcelRow(Row row, AtomicInteger counter) {
        row.createCell(counter.getAndIncrement()).setCellValue(referredFrom);
        row.createCell(counter.getAndIncrement()).setCellValue(referredTo);
        row.createCell(counter.getAndIncrement()).setCellValue(reason);
        row.createCell(counter.getAndIncrement()).setCellValue(createdAt);
        row.createCell(counter.getAndIncrement()).setCellValue(doctor);
        row.createCell(counter.getAndIncrement()).setCellValue(status);
        row.createCell(counter.getAndIncrement()).setCellValue(
                statusLastModifiedAt == null
                        ? ""
                        : Utility.convertUTCDateToJalali(statusLastModifiedAt)
        );
    }
}
