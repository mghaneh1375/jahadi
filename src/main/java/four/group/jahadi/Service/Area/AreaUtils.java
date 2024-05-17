package four.group.jahadi.Service.Area;

import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Trip;
import org.bson.types.ObjectId;

import java.util.Date;

public class AreaUtils {

    public static Area findArea(Trip trip, ObjectId areaId, ObjectId userId) {
        return trip
                .getAreas().stream()
                .filter(area -> area.getId().equals(areaId) && area.getOwnerId().equals(userId))
                .findFirst().orElseThrow(RuntimeException::new);
    }

    public static Area findStartedArea(Trip trip, ObjectId areaId) {

        Area foundArea = trip
                .getAreas().stream()
                .filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        if(foundArea.getDates() == null ||
                foundArea.getDates().get(foundArea.getDates().size() - 1).getEnd() != null ||
                foundArea.getDates().get(foundArea.getDates().size() - 1).getStart().after(new Date())
        )
            throw new RuntimeException("اردو در وضعیت شروع فعالیت ها قرار ندارد");

        return foundArea;
    }

}
