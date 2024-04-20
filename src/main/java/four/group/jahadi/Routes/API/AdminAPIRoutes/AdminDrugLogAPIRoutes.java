package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.DTO.DrugLogFilter;
import four.group.jahadi.Models.DrugLog;
import four.group.jahadi.Service.DrugLogService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/drugs/drug_log")
@Validated
public class AdminDrugLogAPIRoutes {

  @Autowired
  private DrugLogService drugLogService;


   @GetMapping(value = "getLogs")
   @ResponseBody
   @Operation(
    summary = "گرفتن داده های ورود و خروج دارو برای گزارش گیری",
    description = "فیلترهای موجود تاریخ آغاز بازه مدنظر و پایان، دارو مدنظر و همچنین اینکه تنها داده های مربوط به ورود دارو برگردد یا خروج آن می باشند که همگی اختیاری هستند"
   )
    public ResponseEntity<List<DrugLog>> getLogs(
      @RequestBody @Valid DrugLogFilter filters
    ) {
        return drugLogService.list(filters);
    }

}
