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
    private String desc;
    private String report;
    private String reason;
    private String time;
    private String sampleInfoDesc;
    private Integer fileIndex;
}
