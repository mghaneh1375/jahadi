package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Tests.Modules.SubModules.Gharbal.*;
import four.group.jahadi.Tests.Modules.SubModules.Gharbal.Audiologists;

import java.util.List;

import static four.group.jahadi.Tests.Modules.ModuleSeeder.commonSubModules;

public class GharbalgariSeeder {

    public static List<Module> seed() {
        return List.of(
                Module
                        .builder()
                        .name("غربالگری پایه")
                        .tabName("غربالگری")
                        .icon("")
                        .subModules(
                                List.of(
                                        General.make(),
                                        Sight.make(),
                                        commonSubModules.get("experiment"),
                                        commonSubModules.get("internalReferral"),
                                        Audiologists.make(),
                                        Mama.make()
                                )
                        )
                        .isReferral(true)
                        .build(),

                Module
                        .builder()
                        .name("غربالگری روان")
                        .tabName("غربالگری")
                        .icon("")
                        .subModules(
                                List.of(
                                        RavanSeeder.make()
                                )
                        )
                        .isReferral(true)
                        .build()
        );
    }
}
