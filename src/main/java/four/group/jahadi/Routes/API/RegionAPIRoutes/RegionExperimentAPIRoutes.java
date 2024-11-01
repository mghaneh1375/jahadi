package four.group.jahadi.Routes.API.RegionAPIRoutes;

import four.group.jahadi.DTO.ModuleForms.ExperimentalFormDTO;
import four.group.jahadi.Models.Area.PatientExperiment;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.ExperimentServiceInArea;
import four.group.jahadi.Utility.PairValue;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/region/experiment")
@Validated
public class RegionExperimentAPIRoutes extends Router {

    @Autowired
    private ExperimentServiceInArea experimentServiceInArea;

    @GetMapping(value = "list")
    @ResponseBody
    @Operation(summary = "آزمایش های موجود در منطقه")
    public ResponseEntity<List<PairValue>> list() {
        return experimentServiceInArea.list();
    }

    @PostMapping(value = "suggestExperiment/{areaId}/{moduleId}/{patientId}")
    @ResponseBody
    @Operation(summary = "تجویز آزمایش در منطقه توسط دکتر و یا مسئول منطقه")
    public void suggestExperiment(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId moduleId,
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            @RequestBody @Valid ExperimentalFormDTO dto
    ) {
        experimentServiceInArea.suggest(
                getId(request), areaId, moduleId, patientId, dto
        );
    }

    @DeleteMapping(value = "removeExperiment/{areaId}/{moduleId}/{patientId}/{experimentId}")
    @ResponseBody
    @Operation(summary = "حذف تجویز آزمایش در منطقه توسط دکتر و یا مسئول منطقه")
    public void removeExperiment(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId moduleId,
            @PathVariable @ObjectIdConstraint ObjectId patientId,
            @PathVariable @ObjectIdConstraint ObjectId experimentId
    ) {
        experimentServiceInArea.removeExperiment(
                getId(request), areaId, moduleId, patientId, experimentId
        );
    }

    @GetMapping(value = "getExperiments/{areaId}/{moduleId}/{patientId}")
    @ResponseBody
    @Operation(summary = "گرفتن تجویز آزمایشهای یک بیمار مشخص در یک ماژول خاص در منطقه توسط دکتر و یا مسئول منطقه")
    public ResponseEntity<List<PatientExperiment>> getExperiments(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId moduleId,
            @PathVariable @ObjectIdConstraint ObjectId patientId
    ) {
        return experimentServiceInArea.getExperiments(
                getId(request), areaId, moduleId, patientId
        );
    }

    @GetMapping(value = "getAllExperimentsOfPatient/{areaId}/{patientId}")
    @ResponseBody
    @Operation(summary = "گرفتن آزمایشهای یک بیمار مشخص در منطقه توسط دکتر و یا مسئول منطقه")
    public ResponseEntity<List<PatientExperiment>> getAllExperimentsOfPatient(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @PathVariable @ObjectIdConstraint ObjectId patientId
    ) {
        return experimentServiceInArea.getAllExperimentsOfPatient(
                getId(request), areaId, patientId
        );
    }

//    @GetMapping(value = "list/{areaId}")
//    @ResponseBody
//    @Operation(summary = "آزمایش های موجود در منطقه")
//    public ResponseEntity<List<ExperimentInArea>> list(
//            HttpServletRequest request,
//            @PathVariable @ObjectIdConstraint ObjectId areaId) {
//        return experimentServiceInArea.list(
//                getId(request), areaId
//        );
//    }

//    @PutMapping(value = "addExperimentToRegion/{areaId}")
//    @ResponseBody
//    @Operation(summary = "افزودن یک یا چند آزمایش به منطقه")
//    public void store(
//            HttpServletRequest request,
//            @PathVariable @ObjectIdConstraint ObjectId areaId,
//            @RequestBody @Valid @Size(min = 1) ValidList<ObjectId> experiments
//            ) {
//        experimentServiceInArea.addAllToExperimentsList(
//                getId(request), areaId, experiments
//        );
//    }
//
//    @DeleteMapping(value = "removeExperimentFromRegion/{areaId}")
//    @ResponseBody
//    @Operation(summary = "حذف یک یا چند آزمایش از منطقه")
//    public void remove(
//            HttpServletRequest request,
//            @PathVariable @ObjectIdConstraint ObjectId areaId,
//            @RequestBody @Valid @Size(min = 1) ValidList<ObjectId> experiments
//    ) {
//        experimentServiceInArea.removeAllFromExperimentsList(
//                getId(request), areaId, experiments
//        );
//    }
}
