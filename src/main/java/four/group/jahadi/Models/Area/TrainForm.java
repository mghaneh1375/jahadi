package four.group.jahadi.Models.Area;

import four.group.jahadi.Enums.Module.Shepesh;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainForm {

    private Integer height;
    private Integer weight;
    private Double BMI;
    private Shepesh shepesh;

    @Field(value = "recv_culture_package")
    private Boolean recvCulturePackage;

    @Field(value = "recv_shampoo")
    private Boolean recvShampoo;

    private String description;
}
