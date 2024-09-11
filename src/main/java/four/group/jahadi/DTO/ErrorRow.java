package four.group.jahadi.DTO;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorRow {
    private Integer rowIndex;
    private String errorMsg;
}