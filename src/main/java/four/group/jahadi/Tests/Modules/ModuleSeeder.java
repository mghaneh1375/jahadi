package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Repository.ModuleRepository;
import four.group.jahadi.Tests.Modules.SubModules.ParaClinic;
import four.group.jahadi.Tests.Modules.SubModules.*;
import org.bson.types.ObjectId;

import java.util.HashMap;

public class ModuleSeeder {

    public static ObjectId doctorModuleId;
    public static ObjectId miniParaClinicId;


    public static void seed(ModuleRepository moduleRepository) {

        miniParaClinicId = new ObjectId();

        for (Module module : DoctorSeeder.seed()) {
            if(doctorModuleId == null)
                doctorModuleId = module.getId();
            moduleRepository.insert(module);
        }

        moduleRepository.insert(ParaClinic.seed(doctorModuleId, miniParaClinicId));

        for (Module module : EmpowermentSeeder.seed())
            moduleRepository.insert(module);

        for (Module module : GharbalgariSeeder.seed())
            moduleRepository.insert(module);

        for(Module module : ExpertiseSeeder.seed())
            moduleRepository.insert(module);

    }

}
