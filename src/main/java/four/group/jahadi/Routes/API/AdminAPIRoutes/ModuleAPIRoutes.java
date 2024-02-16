package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.DTO.ModuleData;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.PaginatedResponse;
import four.group.jahadi.Service.ModuleService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static four.group.jahadi.Utility.StaticValues.JSON_OK;
import static four.group.jahadi.Utility.Utility.generateErr;
import static four.group.jahadi.Utility.Utility.generateSuccessMsg;

@RestController
@RequestMapping(path = "/api/admin/module")
@Validated
public class ModuleAPIRoutes {

    @Autowired ModuleService moduleService;

    @PostMapping(value = "store")
    @ResponseBody
    public String store(final @RequestBody @Valid ModuleData moduleData) {
        return moduleService.store(moduleData);
    }

    @PutMapping(value = "update/{id}")
    @ResponseBody
    public String update(@PathVariable @ObjectIdConstraint ObjectId id,
                         final @RequestBody @Valid ModuleData moduleData
    ) {
        return moduleService.update(id, moduleData);
    }

    @DeleteMapping(value = "remove/{id}")
    @ResponseBody
    public String remove(@PathVariable @ObjectIdConstraint ObjectId id) {
        moduleService.remove(id);
        return JSON_OK;
    }

    @GetMapping(value = "getAll")
    @ResponseBody
    public PaginatedResponse<Module> getAll(@RequestParam(required = false, name = "section") String section) {

        List<String> filters = new ArrayList<>();
        filters.add("canSuggestDrug|eq|false");

        if(section != null) {
            filters.add("section|eq|" + section);
        }

        return moduleService.list(filters);
    }

}
