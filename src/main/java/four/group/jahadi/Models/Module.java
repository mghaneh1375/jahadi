package four.group.jahadi.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "module")
@Builder
public class Module extends Model {

    private String name;

    @Field("tab_name")
    private String tabName;

    @Field("sub_modules")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SubModule> subModules;

    @Field("has_access_to_full_docs")
    private boolean hasAccessToFullDocs;

    @Field("has_access_to_upload_doc")
    private boolean hasAccessToUploadDoc;

    private String icon;

    @Field("is_referral")
    private boolean isReferral = true;

    @Field("inTrip")
    @JsonIgnore
    private boolean inTrip = true;

}
