package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Tests.Modules.SubModules.Sampler.Step1;
import four.group.jahadi.Tests.Modules.SubModules.Sampler.Step2;

import java.util.List;

public class Sampler {
    public static Module seed() {
        return Module
                .builder()
                .name("نمونه گیر")
                .icon("")
                .tabName("آزمایشگاه")
                .canSuggestExperiment(true)
                .canSuggestDrug(false)
                .isReferral(false)
                .subModules(List.of(
                        Step1.make(),
                        Step2.make()
                ))
                .build();
    }
}
