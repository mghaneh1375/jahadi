package four.group.jahadi.Models.Area;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class PatientExternalForm {
    private String referredFrom;
    private String referredTo;
    private String reason;
    private String createdAt;
    private String doctor;
    private String status;
}
