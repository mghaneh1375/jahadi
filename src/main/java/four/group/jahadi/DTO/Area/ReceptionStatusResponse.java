package four.group.jahadi.DTO.Area;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceptionStatusResponse {
    private Boolean receptionStatus;
    private Boolean canDownloadExcel;
}
