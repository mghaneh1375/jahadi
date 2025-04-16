package four.group.jahadi.Models.Area;

import four.group.jahadi.Enums.Module.Shepesh;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import static four.group.jahadi.Utility.Utility.printNullableField;

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

    @Override
    public String toString() {
        return "{" +
                "\"height\":" + height +
                ", \"weight\":" + weight +
                ", \"BMI\":" + BMI +
                ", \"shepesh\":" + printNullableField(shepesh) +
                ", \"recvCulturePackage\":" + recvCulturePackage +
                ", \"recvShampoo\":" + recvShampoo +
                ", \"description\":" + printNullableField(description) +
                '}';
    }
}
