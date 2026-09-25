package four.group.jahadi.Repository.Area.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientAreaReport {
    private Integer acceptedSections;
    private List<TripInfo> tripInfo;
    private PatientInfoDigest patientInfo;
}
