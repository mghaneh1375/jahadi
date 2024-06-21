package four.group.jahadi.Models.Question;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Boolean canWriteDesc = false;

    @Builder.Default
    @Field("can_update_file")
    private Boolean canUploadFile = false;

    @Builder.Default
    @JsonIgnore
    private Boolean markable = false;

    @JsonIgnore
    private HashMap<String, Integer> marks;
}
