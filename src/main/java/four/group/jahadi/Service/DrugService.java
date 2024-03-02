package four.group.jahadi.Service;

import four.group.jahadi.DTO.DrugData;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Repository.DrugRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DrugService extends AbstractService<Drug, DrugData> {

    @Autowired
    private DrugRepository drugRepository;

    @Override
    public ResponseEntity<List<Drug>> list(Object ... filters) {
        List<Drug> drugs;
        boolean isAdmin = (boolean) filters[0];
        if(filters.length > 1) {
            String searchKey = filters[1].toString();
            drugs = isAdmin ? drugRepository.findLikeName() : drugRepository.findLikeNameAndVisible();
        }
        else {
            drugs = isAdmin ? drugRepository.findAllDigests() : drugRepository.findVisibleDigests();
        }
        
        return new ResponseEntity<>(
                drugs,
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<Drug> findById(ObjectId id, Object ... params) {
        return new ResponseEntity<>(
                drugRepository.findById(id).orElseThrow(InvalidIdException::new),
                HttpStatus.OK
        );
    }

    public ResponseEntity<List<Drug>> findReplacements(ObjectId id) {
        
        Drug drug = drugRepository.findById(id).orElseThrow(InvalidIdException::new);
        List<Drug> replacements = drugRepository.findByIds(drug.getReplacements());
        
        return new ResponseEntity<>(
                replacements,
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<Drug> store(DrugData data, Object ... params) {
        return new ResponseEntity<>(drugRepository.insert(populateEntity(null, data)), HttpStatus.OK);
    }

    @Override
    public void update(ObjectId id, DrugData drugData, Object ... params) {

        Drug drug = drugRepository.findById(id).orElseThrow(InvalidIdException::new);
        drugRepository.save(populateEntity(drug, drugData));
    }

    @Override
    Drug populateEntity(Drug drug, DrugData drugData) {

        if(drug == null)
            drug = new Drug();

        drug.setName(drugData.getName());
        drug.setPrice(drugData.getPrice());

        return drug;
    }

    public void remove(ObjectId id) {
        drugRepository.deleteById(id);
    }
}
