package four.group.jahadi.DTO.Area;

import four.group.jahadi.Enums.Drug.AmountOfUse;
import four.group.jahadi.Enums.Drug.DrugType;
import four.group.jahadi.Enums.Drug.HowToUse;
import four.group.jahadi.Enums.Drug.UseTime;
import four.group.jahadi.Models.Area.PatientDrugJoinModel;
import four.group.jahadi.Utility.Utility;
import lombok.*;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDrugJoinDto {
    private String drugName;
    private String producer;
    private String dose;
    private Integer giveCount;
    private int suggestCount;
    private String patientName;
    private String patientIdentifier;
    private DrugType drugType;
    private AmountOfUse amountOfUse;
    private HowToUse howToUse;
    private UseTime useTime;
    private String giverName;
    private String doctorName;
    private String description;
    private String giveDescription;
    private String givenDrugName;
    private LocalDateTime suggestAt;
    private LocalDateTime giveAt;

    public static PatientDrugJoinDto mapModelToDto(PatientDrugJoinModel model) {
        return PatientDrugJoinDto
                .builder()
                .drugName(model.getDrugInfo().getName())
                .dose(model.getDrugInfo().getDose())
                .producer(model.getDrugInfo().getProducer())
                .drugType(model.getDrugInfo().getDrugType())
                .giveCount(model.getGiveCount())
                .suggestCount(model.getSuggestCount())
                .description(model.getDescription())
                .giveDescription(model.getGiveDescription())
                .useTime(model.getUseTime())
                .amountOfUse(model.getAmountOfUse())
                .howToUse(model.getHowToUse())
                .suggestAt(model.getCreatedAt())
                .giveAt(model.getGiveAt())
                .giverName(model.getGiverInfo())
                .patientName(model.getPatientInfo().getName())
                .patientIdentifier(model.getPatientInfo().getIdentifier())
                .doctorName(model.getDoctorInfo())
                .build();
    }

    public void fillRowExcelFromDto(Row r) {
        r.createCell(0).setCellValue(this.patientName);
        r.createCell(1).setCellValue(this.patientIdentifier);
        r.createCell(2).setCellValue(this.drugName);
        r.createCell(3).setCellValue(this.suggestCount);
        r.createCell(4).setCellValue(this.drugType.getName());
        r.createCell(5).setCellValue(this.dose);
        r.createCell(6).setCellValue(this.producer);
        r.createCell(7).setCellValue(this.useTime.getFaTranslate());
        r.createCell(8).setCellValue(this.amountOfUse.getFaTranslate());
        r.createCell(9).setCellValue(this.howToUse.getFaTranslate());
        r.createCell(10).setCellValue(this.description);
        r.createCell(11).setCellValue(this.doctorName);
        r.createCell(12).setCellValue(Utility.convertUTCDateToJalali(this.suggestAt));
        r.createCell(13).setCellValue(this.givenDrugName);
        r.createCell(14).setCellValue(this.giveCount == null ? 0 : this.giveCount);
        r.createCell(15).setCellValue(this.giverName);
        r.createCell(16).setCellValue(
                this.giveAt == null
                        ? ""
                        : Utility.convertUTCDateToJalali(this.giveAt)
        );
        r.createCell(17).setCellValue(this.giveDescription);
    }
}
