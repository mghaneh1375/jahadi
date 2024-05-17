package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Repository.ModuleRepository;
import four.group.jahadi.Tests.Modules.SubModules.*;
import four.group.jahadi.Tests.Modules.SubModules.ParaClinic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModuleSeeder {

    public static HashMap<String, SubModule> commonSubModules = new HashMap<>();

    public static void seed(ModuleRepository moduleRepository) {

        commonSubModules.put("visit", Visit.make());
        commonSubModules.put("experiment", Experiment.make());
        commonSubModules.put("externalReferral", ExternalReferral.make());
        commonSubModules.put("internalReferral", InternalReferral.make());
        commonSubModules.put("remoteReferral", RemoteReferral.make());
        commonSubModules.put("paraClinic", ParaClinic.make());

        moduleRepository.insert(SightSeeder.seed());
        moduleRepository.insert(Audiologists.seed());
        moduleRepository.insert(DoctorSeeder.seed());
        moduleRepository.insert(PostDoctor.seed());
        moduleRepository.insert(four.group.jahadi.Tests.Modules.ParaClinic.seed());
        moduleRepository.insert(RemoteDoctorSeeder.seed());
        moduleRepository.insert(InsurancerSeeder.seed());
        moduleRepository.insert(GharbalgariSeeder.seed());
    }

}
