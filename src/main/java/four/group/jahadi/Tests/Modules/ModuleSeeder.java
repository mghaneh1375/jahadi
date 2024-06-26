package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Repository.ModuleRepository;
import four.group.jahadi.Tests.Modules.SubModules.ParaClinic;
import four.group.jahadi.Tests.Modules.SubModules.*;
import org.bson.types.ObjectId;

import java.util.HashMap;

public class ModuleSeeder {

    public static HashMap<String, SubModule> commonSubModules = new HashMap<>();

    public static void seed(ModuleRepository moduleRepository) {

        commonSubModules.put("visit", Visit.make());
        commonSubModules.put("experiment", Experiment.make());
        commonSubModules.put("drug", Drug.make());
        commonSubModules.put("externalReferral", ExternalReferral.make());
        commonSubModules.put("remoteReferral", RemoteReferral.make());

        SubModule miniParaClinic = MiniParaClinic.make();
        commonSubModules.put("paraClinic", miniParaClinic);

        ObjectId doctorModuleId = null;

        for (Module module : DoctorSeeder.seed()) {
            if(doctorModuleId == null)
                doctorModuleId = module.getId();
            moduleRepository.insert(module);
        }

        moduleRepository.insert(ParaClinic.seed(doctorModuleId, miniParaClinic.getId()));

        for (Module module : EmpowermentSeeder.seed())
            moduleRepository.insert(module);

        for (Module module : GharbalgariSeeder.seed())
            moduleRepository.insert(module);
    }

}
