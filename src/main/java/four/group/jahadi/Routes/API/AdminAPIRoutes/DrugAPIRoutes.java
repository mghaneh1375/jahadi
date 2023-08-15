package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.DTO.DrugData;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Service.DrugService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static four.group.jahadi.Utility.StaticValues.JSON_NOT_VALID_ID;
import static four.group.jahadi.Utility.Utility.generateErr;
import static four.group.jahadi.Utility.Utility.generateSuccessMsg;

@RestController
@RequestMapping(path = "/api/admin/drug")
@Validated
public class DrugAPIRoutes {

    @Autowired
    DrugService drugService;

    @PostMapping(value = "store")
    @ResponseBody
    public String store(final @RequestBody @Valid DrugData drugData) {
        return drugService.store(drugData);
    }

    @GetMapping(value = "get/{id}")
    @ResponseBody
    public String get(@PathVariable @ObjectIdConstraint ObjectId id) {

        Drug drug = drugService.findById(id);
        if(drug == null)
            return JSON_NOT_VALID_ID;

        JSONObject jsonObject = new JSONObject()
                .put("id", drug.get_id().toString())
                .put("name", drug.getName());

        return generateSuccessMsg("data", jsonObject);
    }
}
