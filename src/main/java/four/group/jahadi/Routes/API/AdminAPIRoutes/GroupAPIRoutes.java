package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.DTO.DrugData;
import four.group.jahadi.Service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/admin/group")
@Validated
public class GroupAPIRoutes {

//    @Autowired
//    DrugService drugService;

//    @PostMapping(value = "store")
//    @ResponseBody
//    public String store(final @RequestBody @Valid DrugData drugData) {
//        return drugService.store(drugData);
//    }


}
