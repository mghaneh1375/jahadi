package four.group.jahadi.DTO.SignUp;

import four.group.jahadi.Enums.DensityUnitCount;
import four.group.jahadi.Validator.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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



}
