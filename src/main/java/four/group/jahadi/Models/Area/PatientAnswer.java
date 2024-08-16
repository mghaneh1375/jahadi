package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.ObjectIdSerialization;
import four.group.jahadi.Models.PatientDocSerialization;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientAnswer {

    @Field("question_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId questionId;
    private Object answer;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Field("uploaded_file")
    @JsonSerialize(using = PatientDocSerialization.class)
    private String uploadedFile;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String desc;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String report;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String reason;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Field("sample_info_desc")
    private String sampleInfoDesc;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Field("additional_uploaded_file")
    @JsonSerialize(using = PatientDocSerialization.class)
    private String additionalUploadedFile;

}
