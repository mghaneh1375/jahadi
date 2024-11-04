package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Tests.Modules.SubModules.Expertise.WomenServicePlusSeeder;
import four.group.jahadi.Tests.Modules.SubModules.Expertise.WomenServiceSeeder;
import four.group.jahadi.Tests.Modules.SubModules.ExternalReferral;
import four.group.jahadi.Tests.Modules.SubModules.MiniParaClinic;
import four.group.jahadi.Tests.Modules.SubModules.Visit;

import java.util.List;

public class ExpertiseSeeder {

    public static List<Module> seed() {
        return List.of(
                Module
                        .builder()
                        .name("متخصص داخلی/عفونی")
                        .tabName("متخصص ها")
                        .icon("icon-doctor-1")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
                                        MiniParaClinic.make(),
                                        WomenServiceSeeder.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build(),
                Module
                        .builder()
                        .name("متخصص زنان")
                        .tabName("متخصص ها")
                        .icon("icon-doctor-1")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
                                        MiniParaClinic.make(),
                                        WomenServiceSeeder.make(),
                                        WomenServicePlusSeeder.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build(),
                Module
                        .builder()
                        .name("متخصص قلب")
                        .tabName("متخصص ها")
                        .icon("icon-doctor-1")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
                                        MiniParaClinic.make(),
                                        WomenServiceSeeder.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build(),
                Module
                        .builder()
                        .name("متخصص اطفال")
                        .tabName("متخصص ها")
                        .icon("icon-doctor-1")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
                                        MiniParaClinic.make(),
                                        WomenServiceSeeder.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build(),
                Module
                        .builder()
                        .name("متخصص روان")
                        .tabName("متخصص ها")
                        .icon("icon-doctor-1")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
                                        MiniParaClinic.make(),
                                        WomenServiceSeeder.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build(),
                Module
                        .builder()
                        .name("متخصص جراحی، ارتوپدی و اورولوژی")
                        .tabName("متخصص ها")
                        .icon("icon-doctor-1")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
                                        MiniParaClinic.make(),
                                        WomenServiceSeeder.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build(),
                Module
                        .builder()
                        .name("متخصص چشم پزشکی")
                        .tabName("متخصص ها")
                        .icon("icon-doctor-1")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
                                        MiniParaClinic.make(),
                                        WomenServiceSeeder.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build(),
                Module
                        .builder()
                        .name("متخصص گوش و حلق و بینی")
                        .tabName("متخصص ها")
                        .icon("icon-doctor-1")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
                                        MiniParaClinic.make(),
                                        WomenServiceSeeder.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build(),
                Module
                        .builder()
                        .name("متخصص پوست")
                        .tabName("متخصص ها")
                        .icon("icon-doctor-1")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
                                        MiniParaClinic.make(),
                                        WomenServiceSeeder.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build(),
                Module
                        .builder()
                        .name("متخصص طب فیزیکی")
                        .tabName("متخصص ها")
                        .icon("icon-doctor-1")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
                                        MiniParaClinic.make(),
                                        WomenServiceSeeder.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build(),
                Module
                        .builder()
                        .name("فوق تخصص غدد")
                        .tabName("متخصص ها")
                        .icon("icon-doctor-1")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
                                        MiniParaClinic.make(),
                                        WomenServiceSeeder.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build(),
                Module
                        .builder()
                        .name("فوق تخصص گوارش")
                        .tabName("متخصص ها")
                        .icon("icon-doctor-1")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
                                        MiniParaClinic.make(),
                                        WomenServiceSeeder.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build(),
                Module
                        .builder()
                        .name("فوق تخصص کلیه")
                        .tabName("متخصص ها")
                        .icon("icon-doctor-1")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
                                        MiniParaClinic.make(),
                                        WomenServiceSeeder.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build(),
                Module
                        .builder()
                        .name("فوق تخصص خون")
                        .tabName("متخصص ها")
                        .icon("icon-doctor-1")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
                                        MiniParaClinic.make(),
                                        WomenServiceSeeder.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build(),
                Module
                        .builder()
                        .name("فوق تخصص روماتولوژی")
                        .tabName("متخصص ها")
                        .icon("icon-doctor-1")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
                                        MiniParaClinic.make(),
                                        WomenServiceSeeder.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build()
        );
    }

}
