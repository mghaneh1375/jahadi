package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.DTO.DrugData;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Service.DrugService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/admin/drug")
@Validated
public class DrugAPIRoutes {

    @Autowired
    DrugService drugService;

    @PostMapping(value = "store")
    @ResponseBody
    public ResponseEntity<Drug> store(final @RequestBody @Valid DrugData drugData) {
        return drugService.store(drugData);
    }

    @GetMapping(value = "get/{id}")
    @ResponseBody
    public ResponseEntity<Drug> get(@PathVariable @ObjectIdConstraint ObjectId id) {
        return drugService.findById(id);
    }
}
