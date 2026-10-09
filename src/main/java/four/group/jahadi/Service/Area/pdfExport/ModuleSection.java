package four.group.jahadi.Service.Area.pdfExport;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleSection {
    private String title;
    private List<SubModuleSection> subModules;
}