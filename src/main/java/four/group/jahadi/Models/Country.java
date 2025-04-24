package four.group.jahadi.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static four.group.jahadi.Utility.Utility.printNullableField;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Country extends Model {
    private String name;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(this.getId()) +
                ", \"name\":" + printNullableField(name) +
                "}\n";
    }
}
