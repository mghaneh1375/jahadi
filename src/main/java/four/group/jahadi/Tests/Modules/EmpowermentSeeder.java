package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Tests.Modules.SubModules.Sight.Sight;
import four.group.jahadi.Tests.Modules.SubModules.Sight.SightGharbal;

import java.util.List;

import static four.group.jahadi.Tests.Modules.ModuleSeeder.addModule;

public class EmpowermentSeeder {

    public static List<Module> seed() {
        Module m = Sight.seed();
        addModule(m);
        return List.of(
                m,
                SightGharbal.seed(),
                Audiologists.seed(),
                PsychologySeeder.seed()
        );
    }
}
