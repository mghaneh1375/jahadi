package four.group.jahadi.Service.Area.pdfExport;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChecklistRow {
    private final String service;
    private final String status;
    private final String desc;
}