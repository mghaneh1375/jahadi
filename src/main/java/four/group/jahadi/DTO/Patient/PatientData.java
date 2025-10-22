package four.group.jahadi.DTO.Patient;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.DTO.PersianNumberDeserializer;
import four.group.jahadi.Enums.AgeType;
import four.group.jahadi.Enums.IdentifierType;
import four.group.jahadi.Enums.Insurance;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Validator.JustNumeric;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

import static four.group.jahadi.Exception.CommonErrorMessages.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientData {

    @Size(min = 2, max = 255, message = NAME_ERR)
    private String name;

    @Size(min = 2, max = 255, message = FATHER_NAME_ERR)
    private String fatherName;

    private Sex sex;
    private long birthDate;

    @JustNumeric
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String phone;

    @JustNumeric
    @Size(min = 7, max = 13, message = IDENTIFIER_ERR)
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String identifier;

    private IdentifierType identifierType;
    private Insurance insurance;
    private AgeType ageType;

    @Size(min = 2, max = 255, message = JOB_ERR)
    private String job;

    @Size(min = 5, max = 20, message = PATIENT_NO_ERR)
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String patientNo;
}
