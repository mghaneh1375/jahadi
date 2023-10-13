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


}
