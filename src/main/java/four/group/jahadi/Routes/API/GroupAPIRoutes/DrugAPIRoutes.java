package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.DTO.DrugData;
import four.group.jahadi.DTO.ErrorRow;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Models.User;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.DrugService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/group/drug")
@Validated
public class DrugAPIRoutes extends Router {
    @Autowired
    private DrugService drugService;

    @GetMapping(value = "list")
    @ResponseBody
    @Operation(summary = "گرفتن لیست داروها توسط مسئول گروه")
    public ResponseEntity<List<Drug>> list(
            HttpServletRequest request,
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false, name = "minAvailableCount") Integer minAvailableCount,
            @RequestParam(required = false, name = "maxAvailableCount") Integer maxAvailableCount,
            @RequestParam(required = false, name = "drugLocation") String drugLocation,
            @RequestParam(required = false, name = "drugType") String drugType,
            @RequestParam(required = false, name = "fromExpireAt") Date fromExpireAt,
            @RequestParam(required = false, name = "toExpireAt") Date toExpireAt,
            @RequestParam(required = false, name = "boxNo") String boxNo,
            @RequestParam(required = false, name = "shelfNo") String shelfNo
    ) throws UnAuthException, NotActivateAccountException {
        return drugService.list(
                getUser(request).getId(),
                name, minAvailableCount, maxAvailableCount,
                drugLocation, drugType, fromExpireAt, toExpireAt,
                boxNo, shelfNo
        );
    }

    @PostMapping(value = "store")
    @ResponseBody
    @Operation(summary = "افزودن تکی دارو توسط مسئول گروه")
    public ResponseEntity<Drug> store(
            HttpServletRequest request,
            @RequestBody @Valid DrugData dto
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        return drugService.store(dto, user.getId(), user.getGroupId());
    }

    @PostMapping(value = "batchStore", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @Operation(summary = "افزودن دسته ای به وسیله فایل اکسل")
    public ResponseEntity<List<ErrorRow>> batchStore(
            HttpServletRequest request,
            @RequestBody MultipartFile file
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        return drugService.batchStore(file, user.getId(), user.getGroupId());
    }
}
