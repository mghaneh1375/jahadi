package four.group.jahadi.DTO.ModuleForms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientFormData {
    private ObjectId questionId;
    private Object answer;
}
