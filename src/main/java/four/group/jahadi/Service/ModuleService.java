package four.group.jahadi.Service;

import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Repository.ModuleRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    public ResponseEntity<Module> show(ObjectId id) {
        return new ResponseEntity<>(moduleRepository.findById(id).orElseThrow(InvalidIdException::new), HttpStatus.OK);
    }

    public Module findById(ObjectId id) {
        Optional<Module> module = moduleRepository.findById(id);
        return module.orElse(null);
    }

    public ResponseEntity<List<Module>> findAllDigests() {
        return new ResponseEntity<>(moduleRepository.findAllDigests(), HttpStatus.OK);
    }

}
