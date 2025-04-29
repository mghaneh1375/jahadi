package four.group.jahadi.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentsToPatient extends Model {
    private Integer amount;
    @Field("card_no")
    private String cardNo;
    private String action;
    private LocalDateTime date;
}
