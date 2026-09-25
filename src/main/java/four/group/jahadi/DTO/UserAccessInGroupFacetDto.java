package four.group.jahadi.DTO;

import four.group.jahadi.Models.UserAccessInGroupDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccessInGroupFacetDto {
    private List<UserAccessInGroupDto> content;
    private List<CountDto> totalElements;
}
