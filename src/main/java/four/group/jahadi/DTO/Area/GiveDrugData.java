package four.group.jahadi.DTO.Area;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Validated
public class GiveDrugData {
    @NotNull
    @Min(value = 1, message = "مقدار تحویل باید حداقل 1 باشد")
    @Max(value = 1000000, message = "مقدار تحویل باید حداکثر 1000000 باشد")
    private Integer amount;

    @Size(max = 1000, message = "متن توضیح باید حداکثر 1000 کاراکتر باشد")
    private String description;

    private ObjectId drugId;
}
