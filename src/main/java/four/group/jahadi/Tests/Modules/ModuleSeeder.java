package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Repository.ModuleRepository;
import four.group.jahadi.Tests.Modules.SubModules.ParaClinic;
import four.group.jahadi.Tests.Modules.SubModules.*;

import java.util.HashMap;

public class ModuleSeeder {

    public static HashMap<String, SubModule> commonSubModules = new HashMap<>();

    public static void seed(ModuleRepository moduleRepository) {

        commonSubModules.put("visit", Visit.make());
        commonSubModules.put("experiment", Experiment.make());
        commonSubModules.put("drug", Drug.make());
        commonSubModules.put("externalReferral", ExternalReferral.make());
        commonSubModules.put("remoteReferral", RemoteReferral.make());
        commonSubModules.put("paraClinic", ParaClinic.make());

        for (Module module : DoctorSeeder.seed())
            moduleRepository.insert(module);

        for (Module module : EmpowermentSeeder.seed())
            moduleRepository.insert(module);

        for (Module module : GharbalgariSeeder.seed())
            moduleRepository.insert(module);
    }

}
