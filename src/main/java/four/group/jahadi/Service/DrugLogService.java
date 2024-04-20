package four.group.jahadi.Service;

import four.group.jahadi.DTO.DrugLogFilter;
import four.group.jahadi.Models.DrugLog;
import four.group.jahadi.Repository.DrugLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrugLogService {

    @Autowired
    private DrugLogRepository drugLogRepository;

    public ResponseEntity<List<DrugLog>> list(DrugLogFilter filters) {
        return new ResponseEntity<>(
                drugLogRepository.findByFilters(
                  filters.getDrugId(), 
                  filters.getStartAt(), 
                  filters.getEndAt(),
                  filters.getJustPositives(),
                  filters.getJustNegatives()
                ),
                HttpStatus.OK
        );
    }

}
