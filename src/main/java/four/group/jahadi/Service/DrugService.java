package four.group.jahadi.Service;

import four.group.jahadi.Enums.Drug.*;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Repository.DrugLogRepository;
import four.group.jahadi.Repository.DrugRepository;
import four.group.jahadi.Repository.WareHouseAccessForGroupRepository;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class DrugService extends AbstractService<Drug> {

    @Autowired
    private DrugRepository drugRepository;
    @Autowired
    private DrugLogRepository drugLogRepository;
    @Autowired
    private WareHouseAccessForGroupRepository wareHouseAccessForGroupRepository;

    @Override
    public ResponseEntity<List<Drug>> list(Object... filters) {
        ObjectId groupId = (ObjectId) filters[0];
        try {
            String name = filters.length > 1 ? (String) filters[1] : null;
            Integer minAvailableCount = filters.length > 2 ? (Integer) filters[2] : null;
            Integer maxAvailableCount = filters.length > 3 ? (Integer) filters[3] : null;
            DrugLocation drugLocation = filters.length > 4 && filters[4] != null ? DrugLocation.valueOf(filters[4].toString().toUpperCase()) : null;
            DrugType drugType = filters.length > 5 && filters[5] != null ? DrugType.valueOf(filters[5].toString().toUpperCase()) : null;
            Date fromExpireAt = filters.length > 6 ? (Date) filters[6] : null;
            Date toExpireAt = filters.length > 7 ? (Date) filters[7] : null;
            String boxNo = filters.length > 8 ? (String) filters[8] : null;
            String shelfNo = filters.length > 9 ? (String) filters[9] : null;

            return new ResponseEntity<>(
                    drugRepository.findByFilters(
                            groupId, name, minAvailableCount, maxAvailableCount,
                            drugLocation, drugType, fromExpireAt, toExpireAt,
                            boxNo, shelfNo
                    ),
                    HttpStatus.OK
            );
        }
        catch (Exception x) {
            throw new InvalidFieldsException(x.getMessage());
        }
    }

    @Override
    public ResponseEntity<Drug> findById(ObjectId id, Object... params) {
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

    public ResponseEntity<DrugType[]> getDrugTypes() {
        return new ResponseEntity<>(DrugType.values(), HttpStatus.OK);
    }

    public ResponseEntity<List<PairValue>> getDrugAmountOfUseOptions() {
        return new ResponseEntity<>(
                Arrays.stream(AmountOfUse.values())
                        .map(amountOfUse -> new PairValue(amountOfUse.name(), amountOfUse.getFaTranslate()))
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<List<PairValue>> getDrugHowToUseOptions() {
        return new ResponseEntity<>(
                Arrays.stream(HowToUse.values())
                        .map(howToUse -> new PairValue(howToUse.name(), howToUse.getFaTranslate()))
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<List<PairValue>> getDrugUseTimeOptions() {
        return new ResponseEntity<>(
                Arrays.stream(UseTime.values())
                        .map(useTime -> new PairValue(useTime.name(), useTime.getFaTranslate()))
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }
}
