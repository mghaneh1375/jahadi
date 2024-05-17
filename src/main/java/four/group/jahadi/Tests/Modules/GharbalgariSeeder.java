package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Tests.Modules.SubModules.Gharbal.Audiologists;
import four.group.jahadi.Tests.Modules.SubModules.Gharbal.General;
import four.group.jahadi.Tests.Modules.SubModules.Gharbal.Mama;
import four.group.jahadi.Tests.Modules.SubModules.Gharbal.Sight;

import java.util.List;

public class GharbalgariSeeder {

    public static Module seed() {
        return Module
                .builder()
                .name("غربالگری")
                .icon("")
                .subModules(
                        List.of(
                                General.make(),
                                Sight.make(),
                                Audiologists.make(),
                                Mama.make()
                        )
                )
                .build();
    }
}
