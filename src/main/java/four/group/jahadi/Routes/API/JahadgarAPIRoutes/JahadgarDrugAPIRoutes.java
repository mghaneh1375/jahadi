package four.group.jahadi.Routes.API.JahadgarAPIRoutes;

import four.group.jahadi.DTO.Area.AreaDrugsData;
import four.group.jahadi.DTO.DrugBookmarkData;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.DrugBookmark;
import four.group.jahadi.Models.User;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.DrugServiceInArea;
import four.group.jahadi.Service.JahadgarDrugService;
import four.group.jahadi.Utility.ValidList;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping(path = "/api/jahadgar/drug")
@Validated
public class JahadgarDrugAPIRoutes extends Router {

    @Autowired
    private JahadgarDrugService drugService;
    @Autowired
    private DrugServiceInArea drugServiceInArea;

    @PutMapping(value = "bookmark/{drugId}")
    @ResponseBody
    @Operation(summary = "منتخب کردن یک دارو و طریقه مصرف آن توسط دکتر")
    public void bookmark(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId drugId,
            @RequestBody @Valid DrugBookmarkData drugBookmarkData
    ) {
        drugService.store(drugBookmarkData, getId(request), drugId);
    }

    @DeleteMapping(value = "bookmark/{drugId}")
    @ResponseBody
    @Operation(summary = "حذف کردن یک دارو از لیست منتخب های دکتر")
    public void removeFromBookmark(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId drugId
    ) {
        drugService.remove(getId(request), drugId);
    }

    @GetMapping(value = "bookmarks")
    @ResponseBody
    @Operation(summary = "گرفتن لیست داروهای منتخب دکتر")
    public ResponseEntity<List<DrugBookmark>> bookmarks(
            HttpServletRequest request
    ) {
        return drugService.list(getId(request));
    }

    @PutMapping(value = "addAllToDrugsList/{areaId}")
    @ResponseBody
    @Operation(summary = "افزودن یک یا چند دارو به منطقه توسط مسئول گروه")
    public void addAllToDrugsList(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid @Size(min = 1) ValidList<AreaDrugsData> drugsData
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        drugServiceInArea.addAllToDrugsList(
                user.getId(), user.getGroupId(), user.getPhone(),
                areaId, drugsData, false
        );
    }

    @DeleteMapping(value = "removeAllFromDrugsList/{areaId}")
    @Operation(summary = "حذف یک یا چند دارو از منطقه توسط مسئول گروه")
    public void removeAllFromDrugsList(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId areaId,
            @RequestBody @Valid @Size(min = 1) ValidList<ObjectId> drugs
    ) throws UnAuthException, NotActivateAccountException {
        User user = getUser(request);
        drugServiceInArea.removeAllFromDrugsList(
                user.getId(), user.getGroupId(),
                user.getPhone(), areaId, drugs, false
        );
    }

}
