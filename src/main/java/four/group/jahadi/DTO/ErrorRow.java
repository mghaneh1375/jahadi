package four.group.jahadi.DTO;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorRow {
    private Object rowIndex;
    private String errorMsg;
}