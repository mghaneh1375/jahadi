package four.group.jahadi.DTO.SignUp;

import four.group.jahadi.Enums.MedicalExpertise;
import four.group.jahadi.Enums.MedicalSection;
import four.group.jahadi.Validator.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpStep4ForGroupData {

    List<String> screeningSicknesses;

    @Positive
    private Integer publicDoctors;

    @Positive
    private Integer donateMedicine;

    List<MedicalSection> medicalSections;
    List<MedicalExpertise> medicalExpertises;

    private boolean adultEducation = false;
    private boolean childEducation = false;
    private boolean freeGlass = false;
    private boolean freeHearingAids = false;
    private boolean popEsmirTest = false;
    private boolean cancerTest = false;
    private boolean socialWorkAssistance = false;
    private boolean quitAddiction = false;
    private boolean familyPsychology = false;
    private boolean urineAnalysis = false;
    private boolean bloodCellsCountTest = false;
    private boolean bioChemTest = false;
    private boolean hormonTest = false;
    private boolean movementHelpEquipments = false;

    private boolean toothExtraction = false;
    private boolean dentalRestoration = false;
    private boolean toothNerveRemoval = false;
    private boolean gumSurgery = false;
    private String otherDentalServices;

    private boolean surgeryRefer = false;
    private boolean hospitalizationRefer = false;
    private boolean freeRefer = false;
    private String otherReferFields;

}
