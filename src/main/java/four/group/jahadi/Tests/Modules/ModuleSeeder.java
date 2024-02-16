package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Repository.ModuleRepository;

public class ModuleSeeder {

    public static void seed(ModuleRepository moduleRepository) {
        moduleRepository.insert(SightSeeder.seed());
        moduleRepository.insert(Audiologists.seed());
        moduleRepository.insert(GeneralSeeder.seed());
    }

}
