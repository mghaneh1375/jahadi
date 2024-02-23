package four.group.jahadi.Models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

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

}
