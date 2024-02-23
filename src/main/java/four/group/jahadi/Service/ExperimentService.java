package four.group.jahadi.Service;

import four.group.jahadi.DTO.ExperimentData;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Models.Experiment;
import four.group.jahadi.Repository.ExperimentRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperimentService extends AbstractService<Experiment, ExperimentData> {

    @Autowired
    private ExperimentRepository experimentRepository;

    @Override
    public ResponseEntity<List<Experiment>> list(Object... filters) {

        List<Experiment> experiments;

        if(filters.length > 0)
            experiments = experimentRepository.justVisible();
        else
            experiments = experimentRepository.findAll();

        return new ResponseEntity<>(
                experiments,
                HttpStatus.OK
        );
    }

    @Override
    public void update(ObjectId id, ExperimentData dto, Object... params) {

        Experiment experiment = experimentRepository.findById(id).orElseThrow(InvalidIdException::new);

        experiment.setVisibility(dto.getVisibility());
        experiment.setTitle(dto.getTitle());
        experiment.setPriority(dto.getPriority());

        experimentRepository.save(experiment);
    }

    @Override
    public ResponseEntity<Experiment> store(ExperimentData dto, Object... params) {

        Experiment experiment = Experiment
                .builder()
                .title(dto.getTitle())
                .priority(dto.getPriority())
                .visibility(dto.getVisibility())
                .build();

        return new ResponseEntity<>(experiment, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Experiment> findById(ObjectId id, Object... params) {
        return null;
    }
}
