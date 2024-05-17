package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;

import java.util.ArrayList;

public class InsurancerSeeder {
    public static Module seed() {
        return Module
                .builder()
                .name("بیمه گر")
                .icon("")
                .subModules(new ArrayList<>())
                .inTrip(false)
                .isReferral(false)
                .hasAccessToInsuranceList(true)
                .build();
    }
}
