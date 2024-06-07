package four.group.jahadi.DTO.Patient;

import four.group.jahadi.Enums.Module.Shepesh;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainFormData {

    @Min(value = 10, message = "قد نباید کمتر از 10 سانتی متر باشد")
    @Max(value = 300, message = "قد نمی تواند بالای 300 سانتی متر باشد")
    @NotNull
    private Integer height;

    @Min(value = 3, message = "وزن نمی تواند کمتر از 3 کیلوگرم باشد")
    @Max(value = 200, message = "وزن نمی تواند بیشتر از 200 کیلوگرم باشد")
    @NotNull
    private Integer weight;

    @NotNull
    private Shepesh shepesh;

    @NotNull
    private Boolean recvCulturePackage;

    @NotNull
    private Boolean recvShampoo;

    @Size(max = 1000, message = "توضیحات حداکثر باید 1000 کاراکتر باشد")
    private String description;
}
