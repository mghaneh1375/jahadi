package four.group.jahadi.Tests.Modules.SubModules.Sight;

import four.group.jahadi.Models.Module;

import java.util.List;

public class Sight {

    public static Module seed() {
        return Module
                .builder()
                .name("اتاق بینایی")
                .icon("")
                .tabName("توان بخشی")
                .subModules(
                        List.of(
                                SightGharbal.make(),
                                SightRoom.make(),
                                ExternalRefer.make()
                        )
                )
                .build();
    }

}
