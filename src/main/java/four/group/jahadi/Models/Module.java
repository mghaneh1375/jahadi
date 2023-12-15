package four.group.jahadi.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "module")
public class Module extends Model {

    private String name;
    private String section;

    @JsonIgnore
    private boolean hasAccessToFullDocs;
    @JsonIgnore
    private boolean hasAccessToUploadDoc;
    @JsonIgnore
    private boolean canSuggestDrug;

    public void setName(String name) {
        this.name = name;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setHasAccessToFullDocs(boolean hasAccessToFullDocs) {
        this.hasAccessToFullDocs = hasAccessToFullDocs;
    }

    public void setHasAccessToUploadDoc(boolean hasAccessToUploadDoc) {
        this.hasAccessToUploadDoc = hasAccessToUploadDoc;
    }

    public void setCanSuggestDrug(boolean canSuggestDrug) {
        this.canSuggestDrug = canSuggestDrug;
    }

    public String getName() {
        return name;
    }

    public String getSection() {
        return section;
    }

    public boolean isHasAccessToFullDocs() {
        return hasAccessToFullDocs;
    }

    public boolean isHasAccessToUploadDoc() {
        return hasAccessToUploadDoc;
    }

    public boolean isCanSuggestDrug() {
        return canSuggestDrug;
    }
}
