package four.group.jahadi.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AdminSignInData {

    @NotNull
    @Size(min = 2, max = 100)
    private String nid;

}
