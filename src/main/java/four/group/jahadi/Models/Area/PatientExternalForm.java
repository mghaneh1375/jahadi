package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.DateSerialization;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class PatientExternalForm {
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId formId;
    @JsonSerialize(using = DateSerialization.class)
    private LocalDateTime statusLastModifiedAt;
    private String referredFrom;
    private String referredTo;
    private String reason;
    private String createdAt;
    private String doctor;
    private String status;
}
