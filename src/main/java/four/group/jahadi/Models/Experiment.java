package four.group.jahadi.Models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import static four.group.jahadi.Utility.Utility.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "experiment")
@Builder
public class Experiment extends Model {
    private String title;
    private Integer priority;
    private Boolean visibility;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(this.getId()) +
                ", \"createdAt\":" + printNullableDate(this.getCreatedAt()) +
                ", \"title\":" + printNullableField(title) +
                ", \"priority\":" + printNullableInteger(priority) +
                ", \"visibility\":" + visibility +
                "}\n";
    }
}
