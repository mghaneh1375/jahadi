package four.group.jahadi.Service.Area.pdfExport;

import lombok.Getter;

import java.util.List;

@Getter
public class TableRendered extends RenderedQuestion {
    private final String title;
    private final List<String> headers;
    private final List<List<String>> rows;

    public TableRendered(String title, List<String> headers, List<List<String>> rows) {
        super("TABLE");
        this.title = title;
        this.headers = headers;
        this.rows = rows;
    }
}