package four.group.jahadi.Service.Area.pdfExport;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PatientReportHtmlBuilder {

    private static final String CSS =
                    "body {\n" +
                    "  font-family: vazirmatn, sans-serif;\n" +
                    "  font-size: 10pt;\n" +
                    "  color: #222;\n" +
                    "  direction: rtl;\n" +
                    "  text-align: right;\n" +
                    "  line-height: 1.8;\n" +
                    "}\n" +
                    ".patient-header {\n" +
                    "  border: 1px solid #999;\n" +
                    "  padding: 8px 12px;\n" +
                    "  margin-bottom: 14px;\n" +
                    "}\n" +
                    ".patient-header table {\n" +
                    "  width: 100%;\n" +
                    "  border-collapse: collapse;\n" +
                    "}\n" +
                    ".patient-header td {\n" +
                    "  padding: 3px 6px;\n" +
                    "}\n" +
                    ".patient-header .label {\n" +
                    "  font-weight: bold;\n" +
                    "  color: #444;\n" +
                    "}\n" +
                    ".module {\n" +
                    "  page-break-before: always;\n" +
                    "}\n" +
                    "h1.module-title {\n" +
                    "  font-size: 15pt;\n" +
                    "  color: #1a3d6b;\n" +
                    "  border-bottom: 2px solid #1a3d6b;\n" +
                    "  padding-bottom: 4px;\n" +
                    "  margin: 10px 0 14px 0;\n" +
                    "}\n" +
                    "h2.submodule-title {\n" +
                    "  font-size: 12pt;\n" +
                    "  background: #eef2f7;\n" +
                    "  padding: 5px 10px;\n" +
                    "  margin: 14px 0 6px 0;\n" +
                    "  border-right: 4px solid #1a3d6b;\n" +
                    "}\n" +
                    ".visit {\n" +
                    "  margin: 6px 0 12px 0;\n" +
                    "  padding: 6px 10px;\n" +
                    "  border: 1px dashed #ccc;\n" +
                    "}\n" +
                    ".visit-meta {\n" +
                    "  font-size: 9pt;\n" +
                    "  color: #555;\n" +
                    "  margin-bottom: 6px;\n" +
                    "}\n" +
                    ".q {\n" +
                    "  margin: 6px 0;\n" +
                    "}\n" +
                    ".q-label {\n" +
                    "  font-weight: bold;\n" +
                    "}\n" +
                    ".section-title {\n" +
                    "  font-weight: bold;\n" +
                    "  margin: 8px 0 4px 0;\n" +
                    "  color: #1a3d6b;\n" +
                    "}\n" +
                    "table.data {\n" +
                    "  width: 100%;\n" +
                    "  border-collapse: collapse;\n" +
                    "  margin: 6px 0;\n" +
                    "  font-size: 9.5pt;\n" +
                    "}\n" +
                    "table.data th {\n" +
                    "  border: 1px solid #999;\n" +
                    "  padding: 4px 8px;\n" +
                    "  text-align: right;\n" +
                    "  background: #f2f2f2;\n" +
                    "  font-weight: bold;\n" +
                    "}\n" +
                    "table.data td {\n" +
                    "  border: 1px solid #999;\n" +
                    "  padding: 4px 8px;\n" +
                    "  text-align: right;\n" +
                    "}\n";

    public String build(PdfReportModel model) {
        StringBuilder sb = new StringBuilder(16 * 1024);

        sb.append("<!DOCTYPE html>\n");
        sb.append("<html dir=\"rtl\" lang=\"fa\">\n");
        sb.append("<head>\n");
        sb.append("<meta charset=\"UTF-8\"/>\n");
        sb.append("<style>\n").append(CSS).append("</style>\n");
        sb.append("</head>\n");
        sb.append("<body dir=\"rtl\">\n");

        // هدر بیمار
        PatientHeader p = model.getPatient();
        if (p != null) {
            sb.append("<div class=\"patient-header\">\n");
            sb.append("<table>\n");
            sb.append("<tr>\n");
            sb.append("<td class=\"label\">نام بیمار:</td>");
            sb.append("<td>").append(esc(p.getName())).append("</td>\n");
            sb.append("<td class=\"label\">کد پرونده:</td>");
            sb.append("<td>").append(esc(p.getFileCode())).append("</td>\n");
            sb.append("</tr>\n");
            sb.append("<tr>\n");
            sb.append("<td class=\"label\">کد ملی:</td>");
            sb.append("<td>").append(esc(p.getNationalCode())).append("</td>\n");
            sb.append("<td class=\"label\">جنسیت / سن:</td>");
            sb.append("<td>").append(esc(p.getGenderAge())).append("</td>\n");
            sb.append("</tr>\n");
            sb.append("</table>\n");
            sb.append("</div>\n");
        }

        // ماژول‌ها
        List<ModuleSection> modules = model.getModules();
        if (modules != null) {
            boolean firstModule = true;
            for (ModuleSection mod : modules) {
                if (mod == null) continue;
                String moduleClass = firstModule ? "first-module" : "module";
                firstModule = false;

                sb.append("<div class=\"").append(moduleClass).append("\">\n");
                sb.append("<h1 class=\"module-title\">")
                        .append(esc(mod.getTitle()))
                        .append("</h1>\n");

                if (mod.getSubModules() != null) {
                    for (SubModuleSection sm : mod.getSubModules()) {
                        if (sm == null) continue;
                        sb.append("<h2 class=\"submodule-title\">")
                                .append(esc(sm.getTitle()))
                                .append("</h2>\n");

                        if (sm.getVisits() != null) {
                            for (VisitBlock v : sm.getVisits()) {
                                if (v == null) continue;
                                sb.append("<div class=\"visit\">\n");
                                sb.append("<div class=\"visit-meta\">\n");
                                sb.append("<span class=\"field\">پزشک: <b>")
                                        .append(esc(v.getDoctorName()))
                                        .append("</b></span>\n");
                                sb.append("<span class=\"field\">زمان پذیرش: <b>")
                                        .append(esc(v.getReceptionTime()))
                                        .append("</b></span>\n");
                                sb.append("</div>\n");

                                if (v.getQuestions() != null) {
                                    for (RenderedQuestion rq : v.getQuestions()) {
                                        appendQuestion(sb, rq);
                                    }
                                }
                                sb.append("</div>\n");
                            }
                        }
                    }
                }
                sb.append("</div>\n");
            }
        }

        sb.append("</body>\n");
        sb.append("</html>\n");
        return sb.toString();
    }

    private void appendQuestion(StringBuilder sb, RenderedQuestion rq) {
        if (rq == null || rq.getType() == null) return;

        switch (rq.getType()) {
            case "SIMPLE": {
                SimpleRendered s = (SimpleRendered) rq;
                sb.append("<div class=\"q\">\n");
                sb.append("<span class=\"q-label\">")
                        .append(esc(s.getLabel()))
                        .append(":</span>\n");
                sb.append("<span class=\"q-answer\">")
                        .append(esc(s.getAnswer()))
                        .append("</span>\n");
                sb.append("</div>\n");
                break;
            }
            case "CHECKLIST": {
                ChecklistRendered c = (ChecklistRendered) rq;
                sb.append("<div class=\"section-title\">")
                        .append(esc(c.getSectionTitle()))
                        .append("</div>\n");
                sb.append("<table class=\"data\">\n");
                sb.append("<thead>\n");
                sb.append("<tr>\n");
                if (c.getHeaders() != null) {
                    for (String h : c.getHeaders()) {
                        sb.append("<th>").append(esc(h)).append("</th>\n");
                    }
                }
                sb.append("</tr>\n");
                sb.append("</thead>\n");
                sb.append("<tbody>\n");
                if (c.getRows() != null) {
                    for (ChecklistRow row : c.getRows()) {
                        if (row == null) continue;
                        sb.append("<tr>\n");
                        sb.append("<td>").append(esc(row.getService())).append("</td>\n");
                        sb.append("<td>").append(esc(row.getStatus())).append("</td>\n");
                        sb.append("<td>").append(esc(row.getDesc())).append("</td>\n");
                        sb.append("</tr>\n");
                    }
                }
                sb.append("</tbody>\n");
                sb.append("</table>\n");
                break;
            }
            case "TABLE": {
                TableRendered t = (TableRendered) rq;
                sb.append("<div class=\"section-title\">")
                        .append(esc(t.getTitle()))
                        .append("</div>\n");
                sb.append("<table class=\"data\">\n");
                sb.append("<thead>\n");
                sb.append("<tr>\n");
                if (t.getHeaders() != null) {
                    for (String h : t.getHeaders()) {
                        sb.append("<th>").append(esc(h)).append("</th>\n");
                    }
                }
                sb.append("</tr>\n");
                sb.append("</thead>\n");
                sb.append("<tbody>\n");
                if (t.getRows() != null) {
                    for (List<String> row : t.getRows()) {
                        sb.append("<tr>\n");
                        for (String cell : row) {
                            sb.append("<td>").append(esc(cell)).append("</td>\n");
                        }
                        sb.append("</tr>\n");
                    }
                }
                sb.append("</tbody>\n");
                sb.append("</table>\n");
                break;
            }
            case "GROUP": {
                GroupRendered g = (GroupRendered) rq;
                sb.append("<div class=\"section-title\">")
                        .append(esc(g.getSectionTitle()))
                        .append("</div>\n");
                if (g.getChildren() != null) {
                    for (RenderedQuestion child : g.getChildren()) {
                        appendQuestion(sb, child);
                    }
                }
                break;
            }
            default:
        }
    }

    private String normalizePersian(String s) {
        if (s == null) return "";
        return s
                .replace('\u064A', '\u06CC') // ي -> ی
                .replace('\u0649', '\u06CC') // ى -> ی
                .replace('\u0643', '\u06A9'); // ك -> ک
    }

    private String esc(String s) {
        if (s == null) return "";
        s = normalizePersian(s);
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}