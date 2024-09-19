package four.group.jahadi.Service;

import four.group.jahadi.DTO.DrugBookmarkData;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Models.DrugBookmark;
import four.group.jahadi.Repository.DrugBookmarkRepository;
import four.group.jahadi.Repository.DrugRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JahadgarDrugService extends AbstractService<DrugBookmark, DrugBookmarkData> {
    @Autowired
    private DrugBookmarkRepository drugBookmarkRepository;

    @Autowired
    private DrugRepository drugRepository;

    @Override
    public ResponseEntity<List<DrugBookmark>> list(Object... filters) {
        ObjectId userId = (ObjectId) filters[0];
        return new ResponseEntity<>(drugBookmarkRepository.findByUserId(userId), HttpStatus.OK);
    }

    @Override
    public void update(ObjectId id, DrugBookmarkData dto, Object... params) {}

    @Override
    public ResponseEntity<DrugBookmark> store(DrugBookmarkData dto, Object... params) {
        ObjectId userId = (ObjectId) params[0];
        ObjectId drugId = (ObjectId) params[1];
        Drug drug = drugRepository.findById(drugId).orElseThrow(
                InvalidIdException::new
        );

        DrugBookmark drugBookmark = drugBookmarkRepository.insert(
                DrugBookmark
                        .builder()
                        .drugName(drug.getName())
                        .drugId(drugId)
                        .userId(userId)
                        .amountOfUses(dto.getAmountOfUses())
                        .howToUses(dto.getHowToUses())
                        .useTimes(dto.getUseTimes())
                        .build()
        );
        return new ResponseEntity<>(drugBookmark, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DrugBookmark> findById(ObjectId id, Object... params) {
        return null;
    }

    public void remove(ObjectId userId, ObjectId drugId) {
        drugBookmarkRepository.removeByUserIdAndDrugId(userId, drugId);
    }
}
