package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Tests.Modules.SubModules.Gharbal.*;
import four.group.jahadi.Tests.Modules.SubModules.Gharbal.Audiologists;
import org.bson.types.ObjectId;

import java.util.List;


public class GharbalgariSeeder {

    public static List<Module> seed(
            ObjectId doctorOid, ObjectId sightOId,
            ObjectId audiologistOid, ObjectId mamaOid,
            ObjectId ravanOid
    ) {
        return List.of(
                Module
                        .builder()
                        .name("غربالگری پایه")
                        .tabName("غربالگری")
                        .icon("")
                        .subModules(
                                List.of(
                                        General.make(doctorOid),
                                        Sight.make(sightOId),
                                        Audiologists.make(audiologistOid),
                                        Mama.make(mamaOid)
                                )
                        )
                        .isReferral(true)
                        .build(),

                Module
                        .builder()
                        .name("غربالگری روان")
                        .tabName("غربالگری")
                        .icon("")
                        .subModules(
                                List.of(
                                        RavanSeeder.make(ravanOid)
                                )
                        )
                        .isReferral(true)
                        .build()
        );
    }
}
