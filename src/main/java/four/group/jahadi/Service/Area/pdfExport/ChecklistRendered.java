package four.group.jahadi.Service.Area.pdfExport;

import lombok.Getter;

import java.util.List;

@Getter
public class ChecklistRendered extends RenderedQuestion {
    private final String sectionTitle;
    private final List<String> headers;
    private final List<ChecklistRow> rows;

    public ChecklistRendered(String sectionTitle, List<String> headers, List<ChecklistRow> rows) {
        super("CHECKLIST");
        this.sectionTitle = sectionTitle;
        this.headers = headers;
        this.rows = rows;
    }
}