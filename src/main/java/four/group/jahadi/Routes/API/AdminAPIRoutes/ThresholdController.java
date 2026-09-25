package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.DTO.ReportThresholdDto;
import four.group.jahadi.Models.ReportThreshold;
import four.group.jahadi.Service.admin.ThresholdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/admin/thresholds")
@Validated
@RequiredArgsConstructor
public class ThresholdController {

    private final ThresholdService thresholdService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<ReportThreshold> update(
            @RequestBody @NotNull @Valid ReportThresholdDto dto
    ) {
        return ResponseEntity.ok(thresholdService.updateThreshold(dto));
    }
}
