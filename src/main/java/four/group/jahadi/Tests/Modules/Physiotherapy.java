package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Tests.Modules.SubModules.ExternalReferral;
import four.group.jahadi.Tests.Modules.SubModules.Visit;

import java.util.List;

public class Physiotherapy {
    private final static String moduleName = "فیزیوتراپی";

    public static Module seed() {
        return Module
                .builder()
                .name(moduleName)
                .tabName("توان بخشی")
                .icon("")
                .subModules(
                        List.of(
                                Visit.make(moduleName),
                                ExternalReferral.make(moduleName)

                        )
                )
                .build();
    }
}
