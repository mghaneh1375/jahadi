package four.group.jahadi.Service;

import four.group.jahadi.DTO.DrugData;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Models.DrugLog;
import four.group.jahadi.Repository.DrugRepository;
import four.group.jahadi.Repository.DrugLogRepository;
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

    @Autowired
    private DrugLogRepository drugLogRepository;
    
    @Override
    public ResponseEntity<List<Drug>> list(Object ... filters) {

        List<Drug> drugs;
        boolean isAdmin = (boolean) filters[0];

        if(filters.length > 1) {
            String searchKey = filters[1].toString();
            drugs = isAdmin ? drugRepository.findLikeName(searchKey) : drugRepository.findLikeNameAndVisible(searchKey);
        }
        else
            drugs = isAdmin ? drugRepository.findAllDigests() : drugRepository.findVisibleDigests();
        
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

    public void setReplacements(ObjectId id, List<ObjectId> replacements) {
        
        Drug drug = drugRepository.findById(id).orElseThrow(InvalidIdException::new);

        if(drugRepository.countByIds(replacements) != replacements.size())
            throw new InvalidIdException();

        drug.setReplacements(replacements);     
        drugRepository.save(drug);
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
        Drug drug = drugRepository.insert(populateEntity(null, data));
        drugLogRepository.insert(
            DrugLog
                .builder()
                .drugId(drug.getId())
                .amount(drug.getAvailable())
                .desc("ایجاد دارو توسط ادمین")
                .build()
        );
        return new ResponseEntity<>(drug, HttpStatus.OK);
    }

    @Override
    public void update(ObjectId id, DrugData drugData, Object ... params) {
        Drug drug = drugRepository.findById(id).orElseThrow(InvalidIdException::new);
        int oldAvailable = drug.getAvailable();
        drug = populateEntity(drug, drugData);
        if(drug.getAvailable() != oldAvailable) {
            DrugLog
                .builder()
                .drugId(drug.getId())
                .amount(drug.getAvailable() - oldAvailable)
                .desc("ویرایش موجودی دارو توسط ادمین")
                .build();
        }
        drugRepository.save(drug);
    }

    @Override
    Drug populateEntity(Drug drug, DrugData drugData) {

        if(drug == null)
            drug = new Drug();

        drug.setName(drugData.getName());
        drug.setPrice(drugData.getPrice());
        drug.setHowToUse(drugData.getHowToUse());
        drug.setDescription(drugData.getDescription());
        drug.setAvailable(drugData.getAvailable());
        drug.setVisibility(drugData.getVisibility());
        drug.setPriority(drugData.getPriority());

        return drug;
    }

    public void remove(ObjectId id) {
        //todo: remove logs and check for usage in active trip
        drugRepository.deleteById(id);
    }
}
