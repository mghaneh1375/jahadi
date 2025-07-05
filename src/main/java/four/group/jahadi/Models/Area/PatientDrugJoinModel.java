package four.group.jahadi.Models.Area;

import four.group.jahadi.Models.Drug;
import four.group.jahadi.Models.Patient;
import four.group.jahadi.Models.PatientDrug;
import four.group.jahadi.Models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDrugJoinModel extends PatientDrug {
    private Drug drugInfo;
    private String doctorInfo;
    private String giverInfo;
    private Patient patientInfo;
}
