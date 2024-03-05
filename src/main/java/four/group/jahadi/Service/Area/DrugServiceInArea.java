package four.group.jahadi.Service.Area;

import four.group.jahadi.DTO.Area.AreaDrugsData;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.AreaDrugs;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Repository.Area.DrugsInAreaRepository;
import four.group.jahadi.Repository.DrugRepository;
import four.group.jahadi.Repository.TripRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DrugServiceInArea {

    @Autowired
    private DrugRepository drugRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private DrugsInAreaRepository drugsInAreaRepository;

    public ResponseEntity<List<AreaDrugs>> list(ObjectId userId, ObjectId areaId) {

        tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        return new ResponseEntity<>(
                drugsInAreaRepository.findDigestByAreaId(areaId),
                HttpStatus.OK
        );
    }

    public void addAllToDrugsList(ObjectId userId, ObjectId areaId, List<AreaDrugsData> dtoList) {

        tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Iterable<Drug> drugsIter = drugRepository.findAllById(
                dtoList.stream().map(AreaDrugsData::getDrugId).distinct().collect(Collectors.toList())
        );

        List<AreaDrugs> drugs = new ArrayList<>();
        List<ObjectId> existDrugs = drugsInAreaRepository.findDigestByAreaId(areaId)
                .stream().map(AreaDrugs::getDrugId).collect(Collectors.toList());

        while (drugsIter.iterator().hasNext()) {

            Drug drug = drugsIter.iterator().next();

            if (!drug.getVisibility())
                throw new NotAccessException();

            if (existDrugs.contains(drug.getId()))
                throw new RuntimeException("داروی " + drug.getName() + " در منطقه موجود است و امکان افزودن مجدد آن نیست");

            dtoList.stream()
                    .filter(areaDrugsData -> areaDrugsData.getDrugId().equals(drug.getId()))
                    .findFirst().ifPresent(dto -> drugs.add(
                                    AreaDrugs
                                            .builder()
                                            .drugName(drug.getName())
                                            .drugId(drug.getId())
                                            .areaId(areaId)
                                            .totalCount(dto.getTotalCount())
                                            .reminder(dto.getTotalCount())
                                            .build()
                            )
                    );
        }

        if (drugs.size() != dtoList.size())
            throw new RuntimeException("ids are incorrect");

        drugsInAreaRepository.insert(drugs);
    }

    public void removeAllFromDrugsList(ObjectId userId, ObjectId areaId, List<ObjectId> ids) {

        tripRepository.findNotStartedByAreaOwnerId(new Date(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        drugsInAreaRepository.removeAreaDrugsById(ids);
    }

    public void updateDrugReminder(
            ObjectId userId, ObjectId areaId,
            ObjectId id, Integer newReminder
    ) {

        tripRepository.findNotStartedByAreaOwnerId(new Date(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        AreaDrugs areaDrug = drugsInAreaRepository.findById(id).orElseThrow(InvalidIdException::new);
        areaDrug.setReminder(newReminder);
        drugsInAreaRepository.save(areaDrug);
    }

    public void advice(ObjectId areaDrugId, Integer amount) {

        AreaDrugs areaDrug = drugsInAreaRepository
                .findById(areaDrugId)
                .orElseThrow(InvalidIdException::new);

        if(areaDrug.getReminder() < amount)
            throw new RuntimeException("تعداد داروهای باقی مانده کمتر از تعداد درخواستی شما می باشد");

        areaDrug.setReminder(areaDrug.getReminder() - amount);
        drugsInAreaRepository.save(areaDrug);
    }
}
