package four.group.jahadi.Service.Area.pdfExport;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientHeader {
    private String name;
    private String fileCode;
    private String nationalCode;
    private String genderAge;
}