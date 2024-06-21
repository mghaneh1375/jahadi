package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Tests.Modules.SubModules.Sight.Sight;

import java.util.List;

public class EmpowermentSeeder {

    public static List<Module> seed() {
        return List.of(
                Sight.seed(),
                Audiologists.seed()
        );
    }
}
