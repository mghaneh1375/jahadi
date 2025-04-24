package four.group.jahadi.Models;

import lombok.*;

import java.text.SimpleDateFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Country extends Model {
    private String name;

    @Override
    public String toString() {
        return "{" +
                "\"id\": \"" + this.getId() +
                "\", \"name\":\"" + name +
                "\"}\n";
    }
}
