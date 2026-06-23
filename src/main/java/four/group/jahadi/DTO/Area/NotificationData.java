package four.group.jahadi.DTO.Area;


import four.group.jahadi.Enums.NotificationType;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class NotificationData {

    @NotBlank
    @Size(min = 2, max = 100)
    private String title;

    @NotBlank
    @Size(min = 3, max = 1000)
    private String description;

    @NotNull
    private NotificationType type;
}