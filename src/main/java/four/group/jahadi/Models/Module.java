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

    @Field("sub_modules")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SubModule> subModules;

    @Field("has_access_to_full_docs")
    private boolean hasAccessToFullDocs;

    @Field("has_access_to_upload_doc")
    private boolean hasAccessToUploadDoc;

    private String icon;

    @Field("is_referral")
    @JsonIgnore
    private boolean isReferral = true;

    @Field("inTrip")
    @JsonIgnore
    private boolean inTrip = true;

    @Field("has_access_to_insurance_list")
    @JsonIgnore
    private boolean hasAccessToInsuranceList = false;

    @Field("min_age")
    @JsonIgnore
    private Integer minAge;

    @Field("max_age")
    @JsonIgnore
    private Integer maxAge;

}
