package four.group.jahadi.Service.Area;

import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Area.ModuleInArea;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Service.TripService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final TripRepository tripRepository;
    private final TripService tripService;

    @CachePut(value = "modules", key = "#userId + '_' + #areaId + '_' + #tabName")
    public List<ModuleInArea> cacheModulesResult(ObjectId userId, ObjectId areaId, String tabName, List<ModuleInArea> result) {
        return result;
    }

    @Cacheable(value = "modules", key = "#userId + '_' + #areaId + '_' + #tabName")
    public List<ModuleInArea> getCachedModules(ObjectId userId, ObjectId areaId, String tabName) {
        return null;
    }

    @Cacheable(value = "area", key = "#userId + '_' + #areaId + '_' + #moduleId",
            condition = "@tripService.isAreaStarted(#areaId)")
    public Area findAreaInTrip(
            ObjectId userId, ObjectId areaId, ObjectId moduleId
    ) {
        Trip trip = tripRepository.findByAreaIdAndResponsibleIdAndModuleId(
                areaId, userId, moduleId
        ).orElseThrow(NotAccessException::new);

        Area area = AreaUtils.findStartedArea(trip, areaId);
        return area;
    }

}
