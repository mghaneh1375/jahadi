package four.group.jahadi.Service.Area.pdfExport;

import lombok.Getter;

import java.util.List;

@Getter
public class GroupRendered extends RenderedQuestion {
    private final String sectionTitle;
    private final List<RenderedQuestion> children;

    public GroupRendered(String sectionTitle, List<RenderedQuestion> children) {
        super("GROUP");
        this.sectionTitle = sectionTitle;
        this.children = children;
    }
}