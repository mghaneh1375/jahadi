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
public class AdminDrugAPIRoutes {

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
    
    @GetMapping(value = "list")
    @ResponseBody
    @Operation(summary = "گرفتن اطلاعات مختصر داروها و یا سرچ در داروها برای ادمین", description="پارامتر نام دارو که میتواند بخشی از نام دارو هم باشد اختیاری و برای سرچ کردن است که باید حداقل سه کاراکتر باشد")
    public ResponseEntity<List<Drug>> list(@RequestParam(required=false, value="name") String name) {
        
        if(name != null && name.length > 2)
            return drugService.list(true, name);
        
        return drugService.list(true);
    }
}
