package four.group.jahadi.DTO;

import four.group.jahadi.Models.ReportType;
import four.group.jahadi.Validator.EnumValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class ReportThresholdDto {
    @NotNull
    ReportType reportType;
    @NotNull
    @Min(0)
    long criticalValue;
    @NotNull
    @Min(0)
    long midValue;
}
