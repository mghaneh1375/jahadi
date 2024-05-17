package four.group.jahadi.Models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.AgeType;
import four.group.jahadi.Enums.IdentifierType;
import four.group.jahadi.Enums.Insurance;
import four.group.jahadi.Enums.Sex;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "patient")
@Builder
public class Patient extends Model {

    private String name;

    @Field("father_name")
    private String fatherName;

    private Sex sex;

    @Field("birth_date")
    @JsonSerialize(using = DateSerialization.class)
    private Date birthDate;

    private String phone;
    private String identifier;

    @Field("identifier_type")
    private IdentifierType identifierType;

    private Insurance insurance;

    @Field("age_type")
    private AgeType ageType;

    private String job;

    @Field("patient_no")
    private String patientNo;
}
