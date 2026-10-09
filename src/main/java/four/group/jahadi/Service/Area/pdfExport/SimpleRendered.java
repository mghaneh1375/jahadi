package four.group.jahadi.Service.Area.pdfExport;

import lombok.Getter;

@Getter
public class SimpleRendered extends RenderedQuestion {
    private final String label;
    private final String answer;

    public SimpleRendered(String label, String answer) {
        super("SIMPLE");
        this.label = label;
        this.answer = answer;
    }
}