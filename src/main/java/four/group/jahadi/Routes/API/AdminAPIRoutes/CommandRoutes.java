package four.group.jahadi.Routes.API.AdminAPIRoutes;

import four.group.jahadi.Tests.Seeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/admin/command")
@Validated
public class CommandRoutes {

    @Autowired private Seeder seeder;

    @GetMapping(value = "moduleSeeder")
    public void moduleSeeder() {
        seeder.moduleSeeder();
    }

    @GetMapping(value = "addCodeToDrugs")
    public void addCodeToDrugs() {
        seeder.addCodeToDrugs();
    }
}
