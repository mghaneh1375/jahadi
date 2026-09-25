package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.PatientServiceInArea;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashMap;

@RestController
@RequestMapping(path = "/api/group/patient")
@Validated
@RequiredArgsConstructor
public class GroupPatientAPIRoutes extends Router {

    private final PatientServiceInArea patientServiceInArea;

    @GetMapping(value = "list")
    @ResponseBody
    public ResponseEntity<HashMap> list(
            HttpServletRequest request,
            @RequestParam(required = false, value = "tripId") ObjectId tripId,
            @RequestParam(required = false, value = "areaid") ObjectId areaId,
            @RequestParam(value = "needTotalElements", required = false) Boolean needTotalElements,
            @RequestParam(value = "pageIndex") @NotNull @Min(0) @Max(100000) int pageIndex,
            @RequestParam(value = "pageSize") @NotNull @Min(5) @Max(100) int pageSize
    ) {
        return patientServiceInArea.getGroupPatients(
                getGroup(request), tripId, areaId,
                pageIndex, pageSize, null, needTotalElements
        );
    }

}
