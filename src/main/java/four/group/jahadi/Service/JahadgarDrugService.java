package four.group.jahadi.Service;

import four.group.jahadi.DTO.DrugBookmarkData;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Models.Area.JoinedDrugBookmarkWithAreaDto;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Models.DrugBookmark;
import four.group.jahadi.Repository.DrugBookmarkRepository;
import four.group.jahadi.Repository.DrugRepository;
import four.group.jahadi.Repository.WareHouseAccessForGroupRepository;
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
    private WareHouseAccessForGroupRepository wareHouseAccessForGroupRepository;
    @Autowired
    private DrugRepository drugRepository;

    public ResponseEntity<List<JoinedDrugBookmarkWithAreaDto>> list(ObjectId userId, ObjectId areaId) {
        List<JoinedDrugBookmarkWithAreaDto> bookmarkList = drugBookmarkRepository.findByUserIdAndAreaId(userId, areaId);
        bookmarkList.forEach(drugBookmark -> {
            if(drugBookmark.getHowToUse() != null)
                drugBookmark.setHowToUseFa(drugBookmark.getHowToUse().getFaTranslate());
            if(drugBookmark.getAmountOfUse() != null)
                drugBookmark.setAmountOfUseFa(drugBookmark.getAmountOfUse().getFaTranslate());
            if(drugBookmark.getUseTime() != null)
                drugBookmark.setUseTimeFa(drugBookmark.getUseTime().getFaTranslate());
        });
        return new ResponseEntity<>(bookmarkList, HttpStatus.OK);
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
                        .amountOfUse(dto.getAmountOfUses())
                        .howToUse(dto.getHowToUses())
                        .useTime(dto.getUseTimes())
                        .amount(dto.getAmount())
                        .build()
        );
        return new ResponseEntity<>(drugBookmark, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DrugBookmark> findById(ObjectId id, Object... params) {
        return null;
    }

    public void remove(ObjectId userId, ObjectId id) {
        drugBookmarkRepository.removeByUserIdAndId(userId, id);
    }

    public ResponseEntity<Boolean> checkAccessToWareHouse(ObjectId groupId, ObjectId userId) {
        return new ResponseEntity<>(
                wareHouseAccessForGroupRepository.existsDrugAccessByGroupIdAndUserId(
                        groupId, userId
                ), HttpStatus.OK
        );
    }
}
