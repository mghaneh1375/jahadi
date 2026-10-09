package four.group.jahadi.Service.Area.pdfExport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class PdfReportModel {
    private PatientHeader patient;
    private List<ModuleSection> modules;
}