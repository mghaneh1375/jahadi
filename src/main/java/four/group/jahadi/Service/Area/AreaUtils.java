package four.group.jahadi.Service.Area;

import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Area.ModuleInArea;
import four.group.jahadi.Models.Trip;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.Objects;

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

        if (foundArea.getDates() == null ||
                foundArea.getDates().get(foundArea.getDates().size() - 1).getEnd() != null ||
                foundArea.getDates().get(foundArea.getDates().size() - 1).getStart().isAfter(LocalDateTime.now())
        )
            throw new RuntimeException("اردو در وضعیت شروع فعالیت ها قرار ندارد");

        return foundArea;
    }

    public static Area findArea(Trip trip, ObjectId areaId) {
        return trip
                .getAreas().stream()
                .filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);
    }

    public static ModuleInArea findModule(Area area, ObjectId moduleId, ObjectId responsibleId, ObjectId secretaryId) {

        ModuleInArea moduleInArea = area
                .getModules().stream()
                .filter(module -> module.getModuleId().equals(moduleId))
                .findFirst().orElseThrow(InvalidIdException::new);

        if ((responsibleId == null && secretaryId == null) ||
                Objects.equals(responsibleId, area.getOwnerId()) ||
                Objects.equals(secretaryId, area.getOwnerId())
        )
            return moduleInArea;

        if(responsibleId != null && secretaryId != null &&
                !moduleInArea.getMembers().contains(responsibleId) &&
                !moduleInArea.getSecretaries().contains(secretaryId)
        )
            throw new NotAccessException();

        boolean findInDoctors = responsibleId != null && moduleInArea.getMembers().contains(responsibleId);
        if(responsibleId != null && !moduleInArea.getMembers().contains(responsibleId))
            throw new NotAccessException();

        if(!findInDoctors && secretaryId != null && !moduleInArea.getSecretaries().contains(secretaryId))
            throw new NotAccessException();

        return moduleInArea;
    }

    public static ModuleInArea findModule(Area area, ObjectId moduleId, ObjectId responsibleId) {

        ModuleInArea moduleInArea = area
                .getModules().stream()
                .filter(module -> module.getModuleId().equals(moduleId))
                .findFirst().orElseThrow(InvalidIdException::new);

        if (responsibleId == null || area.getOwnerId().equals(responsibleId))
            return moduleInArea;

        if(!moduleInArea.getMembers().contains(responsibleId))
            throw new NotAccessException();

        return moduleInArea;
    }

}
