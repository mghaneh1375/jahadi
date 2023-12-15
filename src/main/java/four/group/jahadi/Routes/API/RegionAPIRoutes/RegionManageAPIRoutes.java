package four.group.jahadi.Routes.API.RegionAPIRoutes;

import four.group.jahadi.DTO.Region.RegionRunInfoData;
import four.group.jahadi.DTO.Region.RegionSendNotifData;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.Area.AreaService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "api/region/manage")
@Validated
public class RegionManageAPIRoutes extends Router {

    @Autowired
    AreaService areaService;

    @GetMapping(value = "myCartableAreas")
    @ResponseBody
    @Operation(summary = "گرفتن لیستی از مناطقی که من مسئول منطفه آنها هستم که هنوز تموم نشده اند")
    public ResponseEntity<List<Trip>> myCartableAreas(HttpServletRequest request) {
        return areaService.myCartableList(getId(request));
    }

    @GetMapping(value = "dashboard/{areaId}")
    @ResponseBody
    @Operation(summary = "داشبورد مسئول منطقه", description = "گرفتن داده های موردنیاز برای داشبورد مسئول منطقه زمانی که یک منطقه خاصی را کلیک می کند")
    public ResponseEntity<HashMap<String, Object>> dashboard(HttpServletRequest request,
                                                             @PathVariable @ObjectIdConstraint ObjectId areaId) {
        return areaService.dashboard(getId(request), areaId);
    }

    @PostMapping(value = "sendTripAlarmToAllMembers/{areaId}")
    @ResponseBody
    @Operation(summary = "ارسال پیام به اعضای منطقه توسط مسئول منطقه")
    public void sendTripAlarmToAllMembers(HttpServletRequest request,
                                          @PathVariable @ObjectIdConstraint ObjectId areaId,
                                          @RequestBody @Valid RegionSendNotifData dto) {
        areaService.sendTripAlarmToAllMembers(getId(request), areaId, dto);
    }

    @PutMapping(value = "setRunInfo/{areaId}")
    @ResponseBody
    @Operation(summary = "ست کردن اطلاعات منطقه", description = "ست کردن اطلاعاتی نظیر شهر محل برگزاری، مختصات جغرافیایی و روز و زمان شروع و پایان")
    public void setRunInfo(HttpServletRequest request,
                           @PathVariable @ObjectIdConstraint ObjectId areaId,
                           @RequestBody @Valid RegionRunInfoData regionRunInfoData
    ) {
        areaService.setRunInfo(getId(request), areaId, regionRunInfoData);
    }

    @PutMapping(value = "finalize/{areaId}")
    @ResponseBody
    @Operation(summary = "نهایی سازی ساخت منطقه توسط مسئول منطقه", description = "بعد از آخرین مرحله در تکمیل اطلاعات منطقه (یعنی اختصاص منشی به ماژول ها) باید این api فراخوانی شود تا چک شود آیا تمامی اطلاعات لازم وارد شده است یا نه و منطقه نهایی سازی بشود")
    public void finalizeArea(HttpServletRequest request,
                             @PathVariable @ObjectIdConstraint ObjectId areaId) {
        areaService.finalizeArea(getId(request), areaId);
    }
    // todo start, pause, finish region time

    // todo finalize area defenition
}
