package four.group.jahadi.Models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;

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
                "\"id\":\"" + this.getId() +
                "\", \"createdAt\":\"" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").format(this.getCreatedAt()) +
                "\", \"title\":\"" + title +
                "\", \"priority\":" + priority +
                ", \"visibility\":" + visibility +
                '}';
    }
}
