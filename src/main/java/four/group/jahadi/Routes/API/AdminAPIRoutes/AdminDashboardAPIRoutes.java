package four.group.jahadi.Routes.API.AdminAPIRoutes;


import four.group.jahadi.DTO.dashboard.AdminDashboardData;
import four.group.jahadi.DTO.dashboard.FinantialPerProvinceStatDto;
import four.group.jahadi.DTO.dashboard.PerProvinceStatDto;
import four.group.jahadi.Service.dashboard.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/admin/dashboard")
@Validated
@RequiredArgsConstructor
public class AdminDashboardAPIRoutes {

    private final AdminDashboardService adminDashboardService;

    @GetMapping(value = "/")
    @ResponseBody
    public ResponseEntity<AdminDashboardData> get() {
        return ResponseEntity.ok(
                adminDashboardService.get()
        );
    }

    @GetMapping(value = "/areasPerProvince")
    @ResponseBody
    public ResponseEntity<PerProvinceStatDto> areasPerProvince(
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to
    ) {
        return ResponseEntity.ok(
                adminDashboardService.areasPerProvince(
                        from, to
                )
        );
    }

    @GetMapping(value = "/membersPerProvince")
    @ResponseBody
    public ResponseEntity<PerProvinceStatDto> membersPerProvince(
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to
    ) {
        return ResponseEntity.ok(
                adminDashboardService.membersPerProvince(
                        from, to
                )
        );
    }

    @GetMapping(value = "/patientsPerProvince")
    @ResponseBody
    public ResponseEntity<PerProvinceStatDto> patientsPerProvince(
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to
    ) {
        return ResponseEntity.ok(
                adminDashboardService.patientsPerProvince(
                        from, to
                )
        );
    }

    @GetMapping(value = "/drugsPerProvince")
    @ResponseBody
    public ResponseEntity<PerProvinceStatDto> drugsPerProvince(
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to
    ) {
        return ResponseEntity.ok(
                adminDashboardService.drugsPerProvince(
                        from, to
                )
        );
    }

    @GetMapping(value = "/externalServicesPerProvince")
    @ResponseBody
    public ResponseEntity<PerProvinceStatDto> externalServicesPerProvince(
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to
    ) {
        return ResponseEntity.ok(
                adminDashboardService.externalServicesPerProvince(
                        from, to
                )
        );
    }

    @GetMapping(value = "/finantialExternalServicesPerProvince")
    @ResponseBody
    public ResponseEntity<FinantialPerProvinceStatDto> finantialExternalServicesPerProvince(
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to
    ) {
        return ResponseEntity.ok(
                adminDashboardService.finantialExternalServicesPerProvince(
                        from, to
                )
        );
    }
}
