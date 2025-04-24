package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.AgeType;
import four.group.jahadi.Enums.IdentifierType;
import four.group.jahadi.Enums.Insurance;
import four.group.jahadi.Enums.Sex;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Objects;

import static four.group.jahadi.Utility.Utility.printNullableDate;
import static four.group.jahadi.Utility.Utility.printNullableField;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "patient")
@Builder
public class Patient extends Model {

    private String name;

    @Field("father_name")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String fatherName;

    private Sex sex;

    @Field("birth_date")
    @JsonSerialize(using = DateSerialization.class)
    private Date birthDate;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String phone;
    private String identifier;

    @Field("identifier_type")
    private IdentifierType identifierType;

    private Insurance insurance;

    @Field("age_type")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private AgeType ageType;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String job;

    @Field("patient_no")
    private String patientNo;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(this.getId()) +
                ", \"name\":" + printNullableField(name) +
                ", \"fatherName\":" + printNullableField(fatherName) +
                ", \"sex\":" + printNullableField(sex) +
                ", \"birthDate\":" + printNullableDate(birthDate) +
                ", \"phone\":" + printNullableField(phone) +
                ", \"identifier\":" + printNullableField(identifier) +
                ", \"identifierType\":" + printNullableField(identifierType) +
                ", \"insurance\":" + printNullableField(insurance) +
                ", \"ageType\":" + printNullableField(ageType) +
                ", \"job\":" + printNullableField(job) +
                ", \"patientNo\":" + printNullableField(patientNo) +
                ", \"createdAt\":" + printNullableDate(this.getCreatedAt()) +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(name, patient.name) && Objects.equals(fatherName, patient.fatherName) && sex == patient.sex && Objects.equals(birthDate, patient.birthDate) && Objects.equals(phone, patient.phone) && Objects.equals(identifier, patient.identifier) && identifierType == patient.identifierType && insurance == patient.insurance && ageType == patient.ageType && Objects.equals(job, patient.job) && Objects.equals(patientNo, patient.patientNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, fatherName, sex, birthDate, phone, identifier, identifierType, insurance, ageType, job, patientNo);
    }
}
