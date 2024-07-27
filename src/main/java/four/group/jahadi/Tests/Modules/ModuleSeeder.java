package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Repository.ModuleRepository;
import four.group.jahadi.Tests.Modules.SubModules.ParaClinic;
import four.group.jahadi.Tests.Modules.SubModules.*;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;

public class ModuleSeeder {
    public static ObjectId miniParaClinicId;
    public static HashMap<String, ObjectId> moduleIds;


    public static void seed(ModuleRepository moduleRepository) {

        moduleIds = new HashMap<>();
        miniParaClinicId = new ObjectId();
        List<Module> doctorModules = DoctorSeeder.seed();
        for (Module module : doctorModules) {
            moduleRepository.insert(module);
            moduleIds.put(module.getName(), module.getId());
        }

        moduleRepository.insert(ParaClinic.seed(miniParaClinicId));

        for(Module module : ExpertiseSeeder.seed()) {
            moduleRepository.insert(module);
            moduleIds.put(module.getName(), module.getId());
        }

        for (Module module : EmpowermentSeeder.seed()) {
            moduleRepository.insert(module);
            moduleIds.put(module.getName(), module.getId());
        }

        for (Module module : GharbalgariSeeder.seed())
            moduleRepository.insert(module);
    }

}
