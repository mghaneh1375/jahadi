package four.group.jahadi.Service.Area;

import four.group.jahadi.DTO.Area.AreaDrugsData;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.AreaDrugs;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Models.DrugLog;
import four.group.jahadi.Repository.Area.DrugsInAreaRepository;
import four.group.jahadi.Repository.DrugRepository;
import four.group.jahadi.Repository.DrugLogRepository;
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
    private DrugLogRepository drugLogRepository;
    
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

        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = trip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);
        
        Iterable<Drug> drugsIter = drugRepository.findAllById(
                dtoList.stream().map(AreaDrugsData::getDrugId).distinct().collect(Collectors.toList())
        );

        List<AreaDrugs> drugs = new ArrayList<>();
        List<ObjectId> existDrugs = drugsInAreaRepository.findDigestByAreaId(areaId)
                .stream().map(AreaDrugs::getDrugId).collect(Collectors.toList());

        List<Drug> wantedDrugs = new ArrayList<>();
        List<DrugLog> drugLogs = new ArrayList<>();

        final String msg = "اختصاص به منطقه " + foundArea.getName() + " در اردو " + trip.getName() + " توسط مسئول منطقه ";

        while (drugsIter.iterator().hasNext()) {

            Drug drug = drugsIter.iterator().next();

            if (!drug.getVisibility())
                throw new NotAccessException();

            if (existDrugs.contains(drug.getId()))
                throw new RuntimeException("داروی " + drug.getName() + " در منطقه موجود است و امکان افزودن مجدد آن نیست");
            
            dtoList.stream()
                    .filter(areaDrugsData -> areaDrugsData.getDrugId().equals(drug.getId()))
                    .findFirst().ifPresent(dto -> {
                        
                        if(drug.getAvailable() < dto.getTotalCount())
                            throw new RuntimeException("داروی " + drug.getName() + "ظرفیت این دارو کمتر از مقدار درخواستی شما می باشد");

                        drug.setAvailable(drug.getAvailable() - dto.getTotalCount());
                        wantedDrugs.add(drug);
                        
                        drugLogs.add(
                            DrugLog
                                .builder()
                                .drugId(drug.getId())
                                .amount(-dto.getTotalCount())
                                .desc(msg)
                                .build();
                        );
                        
                        drugs.add(
                                    AreaDrugs
                                            .builder()
                                            .drugName(drug.getName())
                                            .drugId(drug.getId())
                                            .areaId(areaId)
                                            .totalCount(dto.getTotalCount())
                                            .reminder(dto.getTotalCount())
                                            .build()
                            );
                    });
        }

        if (drugs.size() != dtoList.size())
            throw new RuntimeException("ids are incorrect");

        //todo: synchronized op
        drugRepository.saveAll(wantedDrugs);
        drugLogRepository.saveAll(drugLogs);
        drugsInAreaRepository.insert(drugs);
    }

    public void removeAllFromDrugsList(ObjectId userId, ObjectId areaId, List<ObjectId> ids) {

        Trip trip = tripRepository.findNotStartedByAreaOwnerId(new Date(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = trip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);
        
        //todo: synchronized op
        
        List<AreaDrugs> areaDrugs = drugsInAreaRepository.removeAreaDrugsById(ids);
        List<DrugLog> drugLogs = new ArrayList<>();
        List<Drug> wantedDrugs = new ArrayList<>();
        
        Iterable<Drug> drugsIter = drugRepository.findAllById(
            areaDrugs.stream().map(AreaDrugs::getDrugId).distinct().collect(Collectors.toList())
        );

        final String msg = "حذف از منطقه " + foundArea.getName() + " در اردو " + trip.getName() + " توسط مسئول منطقه ";
        
        while (drugsIter.iterator().hasNext()) {
            Drug drug = drugsIter.iterator().next();
            areaDrugs.stream().filter(areaDrug -> areaDrug.getDrugId().equals(drug.getId())).findFirst()
                .ifPresent(areaDrug -> {
                    drug.setAvailable(drug.getAvailable() + areaDrug.getTotalCount());
                    wantedDrugs.add(drug);
                    drugLogs.add(
                        DrugLog
                            .builder()
                            .drugId(drug.getId())
                            .amount(areaDrug.getTotalCount())
                            .desc(msg)
                            .build()
                    );
                });
        }
        
        drugRepository.saveAll(wantedDrugs);
        drugLogRepository.saveAll(drugLogs);
    }

    public void updateDrugReminder(
            ObjectId userId, ObjectId areaId,
            ObjectId id, Integer newReminder
    ) {

        Trip trip = tripRepository.findNotStartedByAreaOwnerId(new Date(), areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = trip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);
        
        AreaDrugs areaDrug = drugsInAreaRepository.findById(id).orElseThrow(InvalidIdException::new);
        int diff = newReminder - areaDrug.reminder;
        Drug drug = drugRepository.findById(areaDrug.getDrugId()).orElseThrow(InvalidIdException::new);
        
        if(diff > 0) { // check availability
            if(drug.getAvailable() < diff)
                throw new RuntimeException("ظرفیت این دارو کمتر از مقدار درخواستی شما می باشد");
        }

        // todo: synchronize action
        areaDrug.setReminder(newReminder);
        drugsInAreaRepository.save(areaDrug);
        
        drug.setAvailable(drug.getAvailable() + diff);
        drugRepository.save(drug);
        
        drogLogRepository.save(
            DrugLog
                .builder()
                .drugId(drug.getId())
                .amount(diff)
                .desc("تغییر موجودی دارو در منطقه " + foundArea.getName() + " در اردو " + trip.getName() + " توسط مسئول منطقه ")
                .build()
        );
    }

    public void advice(ObjectId areaDrugId, Integer amount) {

        AreaDrugs areaDrug = drugsInAreaRepository.findById(areaDrugId)
                .orElseThrow(InvalidIdException::new);

        if(areaDrug.getReminder() < amount)
            throw new RuntimeException("تعداد داروهای باقی مانده کمتر از تعداد درخواستی شما می باشد");

        areaDrug.setReminder(areaDrug.getReminder() - amount);
        drugsInAreaRepository.save(areaDrug);
    }
}
