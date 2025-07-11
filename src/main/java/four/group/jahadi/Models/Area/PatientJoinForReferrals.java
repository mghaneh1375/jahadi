package four.group.jahadi.Models.Area;

import four.group.jahadi.Models.Patient;
import lombok.Data;

@Data
public class PatientJoinForReferrals extends PatientsInArea {
    private Patient patientInfo;
}
