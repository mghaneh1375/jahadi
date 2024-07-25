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

    public static ObjectId doctorModuleId;
    public static ObjectId miniParaClinicId;


    public static void seed(ModuleRepository moduleRepository) {

        miniParaClinicId = new ObjectId();
        List<Module> doctorModules = DoctorSeeder.seed();
        for (Module module : doctorModules)
            moduleRepository.insert(module);

        doctorModuleId = doctorModules.get(0).getId();
        moduleRepository.insert(ParaClinic.seed(doctorModuleId, miniParaClinicId));

        List<Module> empowermentModules = EmpowermentSeeder.seed();

        for (Module module : empowermentModules)
            moduleRepository.insert(module);

        ObjectId sightOId, audiologistOid, mamaOid, ravanOid;
        sightOId = empowermentModules.get(1).getId();
        audiologistOid = empowermentModules.get(2).getId();
        ravanOid = empowermentModules.get(3).getId();

        List<Module> expertiseModules = ExpertiseSeeder.seed();
        for(Module module : expertiseModules)
            moduleRepository.insert(module);

        mamaOid = expertiseModules.get(1).getId();

        for (Module module : GharbalgariSeeder.seed(
                doctorModuleId, sightOId, audiologistOid, mamaOid, ravanOid
        ))
            moduleRepository.insert(module);
    }

}
