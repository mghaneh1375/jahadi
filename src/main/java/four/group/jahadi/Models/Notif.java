package four.group.jahadi.Models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notif")
public class Notif extends ModelWithUser {
    private String msg;
    private boolean seen = false;
}
