package four.group.jahadi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoChangePhoneDAO {
    private String token;
    private Integer code;
}
