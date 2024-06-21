package four.group.jahadi.DTO.Patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientReferralData {
    @Size(max = 1000, message = "توضیحات میتواند حداکثر 1000 کاراکتر باشد")
    private String desc;
}
