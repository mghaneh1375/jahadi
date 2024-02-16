package four.group.jahadi.Routes.API.JahadgarAPIRoutes;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Service.ModuleService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/jahadgar/module")
@Validated
public class JahadgarModuleAPIRoutes {

    @Autowired
    private ModuleService moduleService;

    @GetMapping(path = "get/{id}")
    @ResponseBody
    public ResponseEntity<Module> get(@PathVariable @ObjectIdConstraint ObjectId id) {
        return moduleService.show(id);
    }

}
