package four.group.jahadi.Repository.Area.impl;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.Insurance;
import four.group.jahadi.Models.DateSerialization;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientInfoDigest {
    @Field("_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId id;
    private String name;
    private String identifier;
    private String phone;
    private Insurance insurance;

    @Field("birth_date")
    @JsonSerialize(using = DateSerialization.class)
    private LocalDateTime birthDate;

    @Field("patient_no")
    private String patientNo;
}
