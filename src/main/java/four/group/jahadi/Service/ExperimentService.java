package four.group.jahadi.Service;

import four.group.jahadi.Models.Experiment;
import four.group.jahadi.Repository.ExperimentRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperimentService {

    @Autowired
    private ExperimentRepository experimentRepository;

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

    public ResponseEntity<Experiment> findById(ObjectId id, Object... params) {
        return null;
    }
}
