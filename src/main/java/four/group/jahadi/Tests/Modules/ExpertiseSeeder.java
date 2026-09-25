package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Tests.Modules.SubModules.Expertise.WomenServicePlusSeeder;
import four.group.jahadi.Tests.Modules.SubModules.Expertise.ExpertServiceFormSeeder;
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
                                        Visit.make("متخصص داخلی/عفونی"),
                                        ExternalReferral.make("متخصص داخلی/عفونی"),
                                        MiniParaClinic.make("متخصص داخلی/عفونی"),
                                        ExpertServiceFormSeeder.make("متخصص داخلی/عفونی")
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
                                        WomenServicePlusSeeder.make("متخصص زنان"),
                                        ExternalReferral.make("متخصص زنان"),
                                        ExpertServiceFormSeeder.make("متخصص زنان"),
                                        MiniParaClinic.make("متخصص زنان"),
                                        Visit.make("متخصص زنان")
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build(),
                Module
                        .builder()
                        .name("مامایی")
                        .tabName("متخصص ها")
                        .icon("icon-doctor-1")
                        .subModules(
                                List.of(
                                        WomenServicePlusSeeder.make("مامایی"),
                                        ExternalReferral.make("مامایی"),
                                        ExpertServiceFormSeeder.make("مامایی"),
                                        MiniParaClinic.make("مامایی"),
                                        Visit.make("مامایی")
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
                                        Visit.make("متخصص قلب"),
                                        ExternalReferral.make("متخصص قلب"),
                                        MiniParaClinic.make("متخصص قلب"),
                                        ExpertServiceFormSeeder.make("متخصص قلب")
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
                                        Visit.make("متخصص اطفال"),
                                        ExternalReferral.make("متخصص اطفال"),
                                        MiniParaClinic.make("متخصص اطفال"),
                                        ExpertServiceFormSeeder.make("متخصص اطفال")
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
                                        Visit.make("متخصص روان"),
                                        ExternalReferral.make("متخصص روان"),
                                        MiniParaClinic.make("متخصص روان"),
                                        ExpertServiceFormSeeder.make("متخصص روان")
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
                                        Visit.make("متخصص جراحی، ارتوپدی و اورولوژی"),
                                        ExternalReferral.make("متخصص جراحی، ارتوپدی و اورولوژی"),
                                        MiniParaClinic.make("متخصص جراحی، ارتوپدی و اورولوژی"),
                                        ExpertServiceFormSeeder.make("متخصص جراحی، ارتوپدی و اورولوژی")
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
                                        Visit.make("متخصص چشم پزشکی"),
                                        ExternalReferral.make("متخصص چشم پزشکی"),
                                        MiniParaClinic.make("متخصص چشم پزشکی"),
                                        ExpertServiceFormSeeder.make("متخصص چشم پزشکی")
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
                                        Visit.make("متخصص گوش و حلق و بینی"),
                                        ExternalReferral.make("متخصص گوش و حلق و بینی"),
                                        MiniParaClinic.make("متخصص گوش و حلق و بینی"),
                                        ExpertServiceFormSeeder.make("متخصص گوش و حلق و بینی")
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
                                        Visit.make("متخصص پوست"),
                                        ExternalReferral.make("متخصص پوست"),
                                        MiniParaClinic.make("متخصص پوست"),
                                        ExpertServiceFormSeeder.make("متخصص پوست")
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
                                        Visit.make("متخصص طب فیزیکی"),
                                        ExternalReferral.make("متخصص طب فیزیکی"),
                                        MiniParaClinic.make("متخصص طب فیزیکی"),
                                        ExpertServiceFormSeeder.make("متخصص طب فیزیکی")
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
                                        Visit.make("فوق تخصص غدد"),
                                        ExternalReferral.make("فوق تخصص غدد"),
                                        MiniParaClinic.make("فوق تخصص غدد"),
                                        ExpertServiceFormSeeder.make("فوق تخصص غدد")
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
                                        Visit.make("فوق تخصص گوارش"),
                                        ExternalReferral.make("فوق تخصص گوارش"),
                                        MiniParaClinic.make("فوق تخصص گوارش"),
                                        ExpertServiceFormSeeder.make("فوق تخصص گوارش")
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
                                        Visit.make("فوق تخصص کلیه"),
                                        ExternalReferral.make("فوق تخصص کلیه"),
                                        MiniParaClinic.make("فوق تخصص کلیه"),
                                        ExpertServiceFormSeeder.make("فوق تخصص کلیه")
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
                                        Visit.make("فوق تخصص خون"),
                                        ExternalReferral.make("فوق تخصص خون"),
                                        MiniParaClinic.make("فوق تخصص خون"),
                                        ExpertServiceFormSeeder.make("فوق تخصص خون")
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
                                        Visit.make("فوق تخصص روماتولوژی"),
                                        ExternalReferral.make("فوق تخصص روماتولوژی"),
                                        MiniParaClinic.make("فوق تخصص روماتولوژی"),
                                        ExpertServiceFormSeeder.make("فوق تخصص روماتولوژی")
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .isReferral(true)
                        .build()
        );
    }

}
