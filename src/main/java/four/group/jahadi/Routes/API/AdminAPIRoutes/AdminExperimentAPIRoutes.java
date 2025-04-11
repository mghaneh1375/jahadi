package four.group.jahadi.Routes.API.AdminAPIRoutes;


import four.group.jahadi.Models.Experiment;
import four.group.jahadi.Service.ExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/admin/experiments")
@Validated
public class AdminExperimentAPIRoutes {

    @Autowired
    private ExperimentService experimentService;

    @GetMapping(value = "list")
    @ResponseBody
    public ResponseEntity<List<Experiment>> list() {
        return experimentService.list();
    }

}
