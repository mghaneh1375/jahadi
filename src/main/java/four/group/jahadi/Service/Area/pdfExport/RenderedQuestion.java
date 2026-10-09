package four.group.jahadi.Service.Area.pdfExport;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class RenderedQuestion {
    /** برای تشخیص نوع در Thymeleaf: "SIMPLE" | "CHECKLIST" | "TABLE" | "GROUP" */
    private final String type;
}