package four.group.jahadi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Validated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallHistoryDAO {
    @Size(min = 3, max = 1000, message = "متن توضیح باید بین 3 تا 1000 کاراکتر باشد")
    private String description;
    @NonNull
    private Boolean answered;
    @NonNull
    @Min(value = 1, message = "تعداد تماس ها باید حداقل 1 باشد")
    private Integer counter;
}
