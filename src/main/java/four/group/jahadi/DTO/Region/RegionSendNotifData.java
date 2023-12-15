package four.group.jahadi.DTO.Region;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegionSendNotifData {

    @Size(
            min = 3,
            max = 255,
            message = ""
    )
    private String msg;

    private Boolean sendSMS;
    private Boolean sendNotif;
}
