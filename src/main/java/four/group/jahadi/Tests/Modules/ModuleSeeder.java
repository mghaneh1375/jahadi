package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Repository.ModuleRepository;
import four.group.jahadi.Tests.Modules.SubModules.ParaClinic;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModuleSeeder {
    public static HashMap<String, ObjectId> moduleIds;
    private static ModuleRepository moduleRepository;
    private static List<Module> newItems = new ArrayList<>();

    private static void addModule(Module module) {
        Module sameModuleItr = moduleRepository.findByName(module.getName());
        ObjectId moduleId = module.getId();
        if(sameModuleItr != null) {
            moduleId = sameModuleItr.getId();
            module.setId(moduleId);
        }
        newItems.add(module);
        moduleIds.put(module.getName(), moduleId);
    }

    public static void seed(ModuleRepository moduleRepository) {

        ModuleSeeder.moduleRepository = moduleRepository;
        List<Module> all = moduleRepository.findAll();
        moduleIds = new HashMap<>();

        addModule(ParaClinic.seed());

        for (Module module : DoctorSeeder.seed())
            addModule(module);

        for(Module module : ExpertiseSeeder.seed())
            addModule(module);

        for (Module module : EmpowermentSeeder.seed())
            addModule(module);

        for (Module module : GharbalgariSeeder.seed())
            addModule(module);

        moduleRepository.deleteAll(all);
        moduleRepository.saveAll(newItems);
    }

}
