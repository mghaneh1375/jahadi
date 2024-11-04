package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Tests.Modules.SubModules.ExperimentAnswer;

import java.util.List;

public class ExperimentSeeder {
    public static Module seed() {
        return Module
                .builder()
                .name("آزمایشگاه")
                .tabName("آزمایشگاه")
                .icon("icon-laboratory")
                .canSuggestExperiment(false)
                .canSuggestDrug(false)
                .isReferral(false)
                .subModules(List.of(
                        ExperimentAnswer.make()
                ))
                .build();
    }
}
