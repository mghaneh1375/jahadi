package four.group.jahadi.DTO.Patient;

import four.group.jahadi.Models.Patient;
import four.group.jahadi.Models.PatientDrug;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PatientAdvices {
    private Patient patient;
    private List<PatientDrug> drugs;
    public void addToDrugList(PatientDrug drug) {
        drugs.add(drug);
    }
}
