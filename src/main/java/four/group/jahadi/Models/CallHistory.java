package four.group.jahadi.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallHistory extends Model {
    private String description;
    private Boolean answered;
    private Integer counter;
}
