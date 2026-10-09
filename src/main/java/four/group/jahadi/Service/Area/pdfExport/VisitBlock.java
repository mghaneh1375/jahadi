package four.group.jahadi.Service.Area.pdfExport;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitBlock {
    private String doctorName;
    private String receptionTime;
    private List<RenderedQuestion> questions;
}