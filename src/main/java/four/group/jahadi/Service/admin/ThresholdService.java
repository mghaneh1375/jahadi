package four.group.jahadi.Service.admin;

import four.group.jahadi.DTO.ReportThresholdDto;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Models.ReportThreshold;
import four.group.jahadi.Models.ReportType;
import four.group.jahadi.Repository.ThresholdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ThresholdService {
    @Autowired
    private ThresholdRepository repository;

    @CacheEvict(cacheNames = {
            "thresholds",
            "areasPerProvince",
            "membersPerProvince",
            "patientsPerProvince",
            "drugsPerProvince",
            "externalServicesPerProvince",
            "finantialExternalServicesPerProvince"
    }, allEntries = true)
    public ReportThreshold updateThreshold(ReportThresholdDto dto) {
        if(dto.getMidValue() <= dto.getCriticalValue()) {
            throw new InvalidFieldsException("mid should be grater than critical");
        }
        ReportThreshold threshold = repository.findByReportType(dto.getReportType().name())
                .orElse(new ReportThreshold());
        threshold.setReportType(dto.getReportType());
        threshold.setCriticalValue(dto.getCriticalValue());
        threshold.setMidValue(dto.getMidValue());
        threshold.setUpdatedAt(LocalDateTime.now());
        return repository.save(threshold);
    }

    @Cacheable(value = "thresholds", key = "#reportType")
    public ReportThreshold getThreshold(ReportType reportType) {
        return repository.findByReportType(reportType.name())
                .orElse(ReportThreshold
                        .builder()
                        .midValue(5)
                        .midValue(10)
                        .build());
    }
}
