package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;

import java.util.List;

public class ExperimentTab {
    public static List<Module> seed() {
        return List.of(
                Sampler.seed(),
                ExperimentSeeder.seed()
        );
    }
}
