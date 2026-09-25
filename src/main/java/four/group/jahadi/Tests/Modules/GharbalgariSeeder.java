package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Tests.Modules.SubModules.Gharbal.Audiologists;
import four.group.jahadi.Tests.Modules.SubModules.Gharbal.*;

import java.util.List;

import static four.group.jahadi.Tests.Modules.ModuleSeeder.moduleIds;


public class GharbalgariSeeder {

    public static List<Module> seed() {
        return List.of(
                Module
                        .builder()
                        .name("غربالگری پایه")
                        .tabName("غربالگری")
                        .icon("icon-health-1")
                        .subModules(
                                List.of(
                                        General.make("غربالگری پایه", moduleIds.get("پزشک عمومی")),
                                        Sight.make("غربالگری پایه", moduleIds.get("غربالگری دوم بینایی")),
                                        Audiologists.make("غربالگری پایه", moduleIds.get("شنوایی")),
                                        Mama.make("غربالگری پایه", moduleIds.get("متخصص زنان"))
                                )
                        )
                        .isReferral(true)
                        .build(),

                Module
                        .builder()
                        .name("غربالگری روان")
                        .tabName("غربالگری")
                        .icon("icon-health-1")
                        .subModules(
                                List.of(
                                        RavanSeeder.make("غربالگری روان", moduleIds.get("روانشناس"))
                                )
                        )
                        .isReferral(true)
                        .build()
        );
    }
}
