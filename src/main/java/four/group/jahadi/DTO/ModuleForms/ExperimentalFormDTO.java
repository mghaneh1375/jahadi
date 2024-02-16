package four.group.jahadi.DTO.ModuleForms;

import four.group.jahadi.Enums.Experiment;
import four.group.jahadi.Validator.ModuleForms.ValidatedExperimentalForm;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@ValidatedExperimentalForm
public class ExperimentalFormDTO {

    private Experiment experiment;

    @Size(max = 500)
    private String description;

}
