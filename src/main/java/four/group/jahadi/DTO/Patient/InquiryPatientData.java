package four.group.jahadi.DTO.Patient;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.DTO.PersianNumberDeserializer;
import four.group.jahadi.Enums.IdentifierType;
import four.group.jahadi.Validator.JustNumeric;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

import static four.group.jahadi.Exception.CommonErrorMessages.IDENTIFIER_ERR;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InquiryPatientData {

    @JustNumeric
    @Size(min = 7, max = 13, message = IDENTIFIER_ERR)
    @JsonDeserialize(using = PersianNumberDeserializer.class)
    private String identifier;

    private IdentifierType identifierType;
}
