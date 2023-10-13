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

//        Pageable pageable = PageRequest.of(0, 10);
//
//        Page<Drug> all = drugRepository.findAllWithFilter(Drug.class,
//                FilteringFactory.parseFromParams(filters, Drug.class), pageable
//        );

        return null;
//        return returnPaginateResponse(all);
    }

    @Override
    public ResponseEntity<Drug> findById(ObjectId id, Object ... params) {
        return new ResponseEntity<>(
                drugRepository.findById(id).orElseThrow(InvalidIdException::new),
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
