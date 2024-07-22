package four.group.jahadi.Models.Question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import four.group.jahadi.Utility.PairValue;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@Document
@NoArgsConstructor
@SuperBuilder
public class CheckListGroupQuestion extends Question {
    @Field("section_title")
    private String sectionTitle;
    private List<PairValue> options;
    private List<SimpleQuestion> questions;

    @Builder.Default
    @Field("can_write_desc")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Boolean canWriteDesc = false;

    @Builder.Default
    @Field("can_write_report")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Boolean canWriteReport = false;

    @Builder.Default
    @Field("can_write_reason")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Boolean canWriteReason = false;

    @Builder.Default
    @Field("can_write_sample_info_desc")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Boolean canWriteSampleInfoDesc = false;

    @Builder.Default
    @Field("can_write_time")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Boolean canWriteTime = false;

    @Builder.Default
    @Field("can_update_file")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Boolean canUploadFile = false;

    @Builder.Default
    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Boolean markable = false;

    @JsonIgnore
    private HashMap<String, Integer> marks;
}
