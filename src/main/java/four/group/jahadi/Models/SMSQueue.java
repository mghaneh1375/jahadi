package four.group.jahadi.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sms_queue")
public class SMSQueue extends Model {

    private String msg;
    private String phone;
    private boolean sent = false;

}
