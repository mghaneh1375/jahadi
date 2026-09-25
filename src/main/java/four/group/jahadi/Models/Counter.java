package four.group.jahadi.Models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "counter")
@Builder
public class Counter extends Model {

    @Field(name = "counter_key")
    private String counterKey;

    private String prefix;

    @Field("current_value")
    private Long currentValue;
}
