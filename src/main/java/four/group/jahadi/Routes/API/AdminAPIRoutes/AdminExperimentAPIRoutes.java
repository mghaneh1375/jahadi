package four.group.jahadi.Routes.API.AdminAPIRoutes;


import four.group.jahadi.DTO.ExperimentData;
import four.group.jahadi.Models.Experiment;
import four.group.jahadi.Service.ExperimentService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/admin/experiments")
@Validated
public class AdminExperimentAPIRoutes {

    @Autowired
    private ExperimentService experimentService;

    @GetMapping(value = "list")
    @ResponseBody
    public ResponseEntity<List<Experiment>> list() {
        return experimentService.list();
    }

    @PostMapping(value = "store")
    @ResponseBody
    public ResponseEntity<Experiment> store(@Valid ExperimentData dto) {
        return experimentService.store(dto);
    }

    @PostMapping(value = "update/{id}")
    @ResponseBody
    public void update(
            @PathVariable @ObjectIdConstraint ObjectId id,
            @Valid ExperimentData dto
    ) {
        experimentService.update(id, dto);
    }

}
