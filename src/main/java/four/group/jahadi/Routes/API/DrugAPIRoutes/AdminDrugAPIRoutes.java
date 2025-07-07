package four.group.jahadi.Routes.API.DrugAPIRoutes;

import four.group.jahadi.Models.Drug;
import four.group.jahadi.Service.DrugService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/drugs/drug")
@Validated
public class AdminDrugAPIRoutes {

    @Autowired
    DrugService drugService;

//    @PostMapping(value = "store")
//    @ResponseBody
//    public ResponseEntity<Drug> store(final @RequestBody @Valid DrugData drugData) {
//        return drugService.store(drugData);
//    }

//    @PutMapping(value = "update/{id}")
//    @ResponseBody
//    public void update(
//        final @PathVariable @ObjectIdConstraint ObjectId id,
//        final @RequestBody @Valid DrugData drugData
//    ) {
//        drugService.update(id, drugData);
//    }
    
    @GetMapping(value = "get/{id}")
    @ResponseBody
    public ResponseEntity<Drug> get(@PathVariable @ObjectIdConstraint ObjectId id) {
        return drugService.findById(id);
    }
    
    @GetMapping(value = "list")
    @ResponseBody
    @Operation(summary = "گرفتن اطلاعات مختصر داروها و یا سرچ در داروها برای ادمین", description="پارامتر نام دارو که میتواند بخشی از نام دارو هم باشد اختیاری و برای سرچ کردن است که باید حداقل سه کاراکتر باشد")
    public ResponseEntity<List<Drug>> list(@RequestParam(required=false, value="name") String name) {
        
        if(name != null && name.length() > 2)
            return drugService.list(true, name);
        
        return drugService.list(true);
    }

    @GetMapping(value = "setDrugReplacements/{drugId}")
    @ResponseBody
    @Operation(summary = "ست کردن داروهای جایگزین یک دارو خاص")
    public void setDrugReplacements(
            @PathVariable @ObjectIdConstraint ObjectId drugId,
            @RequestBody List<ObjectId> drugs
    ) {
        drugService.setReplacements(drugId, drugs);
    }

    @GetMapping(value = "fillGroupIdInLogDoc")
    public void fillGroupIdInLogDoc() {
        drugService.fillGroupIdInLogDoc();
    }
}
