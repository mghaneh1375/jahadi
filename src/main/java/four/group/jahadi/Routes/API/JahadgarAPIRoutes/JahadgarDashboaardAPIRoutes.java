package four.group.jahadi.Routes.API.JahadgarAPIRoutes;

import four.group.jahadi.DTO.profile.JahadgarCartable;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.dashboard.JahadgarDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api/jahadgar/dashboard")
@Validated
@RequiredArgsConstructor
public class JahadgarDashboaardAPIRoutes extends Router {

    private final JahadgarDashboardService jahadgarDashboardService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<JahadgarCartable> dashboard(
            HttpServletRequest request
    ) {
        return jahadgarDashboardService.cartable(getId(request));
    }

}
