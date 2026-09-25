package four.group.jahadi.Models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "report_thresholds")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportThreshold extends Model{
    @Field("report_type")
    private ReportType reportType;
    @Field("critical_value")
    private long criticalValue;
    @Field("mid_value")
    private long midValue;
    private LocalDateTime updatedAt;
}
