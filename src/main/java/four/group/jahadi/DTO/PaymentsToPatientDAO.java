package four.group.jahadi.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class PaymentsToPatientDAO {
    @NonNull
    @Size(min = 2, max = 1000, message = "متن اقدامات باید بین 2 و 1000 کاراکتر باشد")
    private String action;
    @NonNull
    @Pattern(regexp = "\\d{16}", message = "شماره کارت نامعتبر است")
    private String cardNo;
    @NonNull
    @Min(value = 0, message = "مبلغ واریزی باید حداقل 0 باشد")
    private Integer amount;
    @NonNull
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;
}
