package four.group.jahadi.Service.Area;

import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Experiment;
import four.group.jahadi.Models.Area.ExperimentInArea;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Repository.ExperimentRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Utility.Utility;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExperimentServiceInArea {

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private TripRepository tripRepository;

    public ResponseEntity<List<ExperimentInArea>> list(ObjectId userId, ObjectId areaId) {

        Trip wantedTrip = tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = wantedTrip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        return new ResponseEntity<>(
                foundArea.getExperiments(),
                HttpStatus.OK
        );
    }

    public void addAllToExperimentsList(ObjectId userId, ObjectId areaId, List<ObjectId> ids) {

        Trip wantedTrip = tripRepository.findNotStartedByAreaOwnerId(Utility.getCurrDate(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        Iterable<Experiment> experimentsIter = experimentRepository.findAllById(ids);
        List<ExperimentInArea> experiments = new ArrayList<>();

        while (experimentsIter.iterator().hasNext()) {

            Experiment experiment = experimentsIter.iterator().next();

            if(!experiment.getVisibility())
                throw new NotAccessException();

            experiments.add(
                    ExperimentInArea
                            .builder()
                            .title(experiment.getTitle())
                            .experimentId(experiment.getId())
                            .build()
            );
        }

        if(experiments.size() != ids.size())
            throw new RuntimeException("ids are incorrect");

        Area foundArea = wantedTrip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        foundArea.getExperiments().addAll(experiments);
        tripRepository.save(wantedTrip);
    }

    public void removeAllFromExperimentsList(ObjectId userId, ObjectId areaId, List<ObjectId> ids) {

        Trip wantedTrip = tripRepository.findNotStartedByAreaOwnerId(Utility.getCurrDate(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = wantedTrip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        foundArea.getExperiments()
                .removeIf(experimentInArea -> ids.contains(experimentInArea.getId()));

        tripRepository.save(wantedTrip);
    }

}
