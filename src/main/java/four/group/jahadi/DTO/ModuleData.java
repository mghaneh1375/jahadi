package four.group.jahadi.DTO;

import four.group.jahadi.Models.Section;
import four.group.jahadi.Validator.ValidatedModule;

@ValidatedModule
public class ModuleData {

    private String name;
    private String section;
    private Boolean hasAccessToFullDocs;
    private Boolean hasAccessToUploadDoc;
    private Boolean canSuggestDrug;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public boolean isHasAccessToFullDocs() {
        return hasAccessToFullDocs;
    }

    public void setHasAccessToFullDocs(boolean hasAccessToFullDocs) {
        this.hasAccessToFullDocs = hasAccessToFullDocs;
    }

    public boolean isHasAccessToUploadDoc() {
        return hasAccessToUploadDoc;
    }

    public void setHasAccessToUploadDoc(boolean hasAccessToUploadDoc) {
        this.hasAccessToUploadDoc = hasAccessToUploadDoc;
    }

    public boolean isCanSuggestDrug() {
        return canSuggestDrug;
    }

    public void setCanSuggestDrug(boolean canSuggestDrug) {
        this.canSuggestDrug = canSuggestDrug;
    }
}
