package four.group.jahadi.Tests.Modules.SubModules.Sight;

import four.group.jahadi.Models.Module;

import java.util.List;

public class Sight {

    public static Module seed() {
        return Module
                .builder()
                .name("اتاق بینایی")
                .icon("icon-disability")
                .tabName("توان بخشی")
                .subModules(
                        List.of(
                                SightRoom.make(),
                                ExternalRefer.make()
                        )
                )
                .isReferral(true)
                .build();
    }

}
