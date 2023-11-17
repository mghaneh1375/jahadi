package four.group.jahadi.DTO.SignUp;

import four.group.jahadi.Enums.DensityUnitCount;
import four.group.jahadi.Validator.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpStep3ForGroupData {


    // density
    private DensityUnitCount densityUnitCount;

    @Positive
    private Integer pullToothCount;

    @Positive
    private Integer restorationToothCount;

    @Positive
    private Integer rooterCanalCount;

    @Positive
    private Integer imagingEquipmentCount;

    // medical

    @Positive
    private Integer barometerCount;

    @Positive
    private Integer glucometerCount;

    @Positive
    private Integer paravanCount;

    @Positive
    private Integer bedCount;

    @Positive
    private Integer monitoringCount;

    @Positive
    private Integer examinationSetCount;

    @Positive
    private Integer ecgCount;

    @Positive
    private Integer electronicShockCount;

    @Positive
    private Integer revivingBagCount;

    // Midwifery

    @Positive
    private Integer popEsmirCount;

    @Positive
    private Integer sonoKidCount;

    @Positive
    private Integer wifeExaminationEquipmentCount;

    @Positive
    private Integer samplingEquipmentCount;

    @Positive
    private Integer geneticallyBedCount;

    // Optometry

    @Positive
    private Integer optometryEquip1;

    @Positive
    private Integer optometryEquip2;

    @Positive
    private Integer optometryEquip3;

    @Positive
    private Integer optometryEquip4;

    @Positive
    private Integer optometryEquip5;

    // Audiologists

    @Positive
    private Integer audiologistEquip1;

    @Positive
    private Integer audiologistEquip2;

    @Positive
    private Integer audiologistEquip3;

    @Positive
    private Integer audiologistEquip4;

    @Positive
    private Integer audiologistEquip5;

    // Imaging

    @Positive
    private Integer imagingEquip1;

    @Positive
    private Integer imagingEquip2;

    @Positive
    private Integer imagingEquip3;

    // Laboratory

    @Positive
    private Integer laboratoryEquip1;

    @Positive
    private Integer laboratoryEquip2;

    @Positive
    private Integer laboratoryEquip3;

    @Positive
    private Integer laboratoryEquip4;

    @Positive
    private Integer laboratoryEquip5;

    // Media

    @Positive
    private Integer mediaEquip1;

    @Positive
    private Integer mediaEquip2;

    @Positive
    private Integer mediaEquip3;

    private HashMap<String, Integer> customEquips;
}
