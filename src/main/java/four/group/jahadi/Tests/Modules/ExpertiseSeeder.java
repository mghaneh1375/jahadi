package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Tests.Modules.SubModules.Expertise.WomenServicePlusSeeder;
import four.group.jahadi.Tests.Modules.SubModules.Expertise.WomenServiceSeeder;
import four.group.jahadi.Tests.Modules.SubModules.ExternalReferral;
import four.group.jahadi.Tests.Modules.SubModules.MiniParaClinic;
import four.group.jahadi.Tests.Modules.SubModules.Visit;

import java.util.List;

public class ExpertiseSeeder {

    public static List<Module> seed() {
        return List.of(
                Module
                        .builder()
                        .name("متخصص ها")
                        .tabName("متخصص ها")
                        .icon("")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
                                        MiniParaClinic.make(),
                                        WomenServiceSeeder.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build(),
                Module
                        .builder()
                        .name("متخصص زنان")
                        .tabName("متخصص ها")
                        .icon("")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
                                        MiniParaClinic.make(),
                                        WomenServiceSeeder.make(),
                                        WomenServicePlusSeeder.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build()
        );
    }

}
