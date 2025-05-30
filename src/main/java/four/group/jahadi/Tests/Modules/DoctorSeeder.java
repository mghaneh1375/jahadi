package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.HaveOrNot;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Tests.Modules.SubModules.ExternalReferral;
import four.group.jahadi.Tests.Modules.SubModules.MiniParaClinic;
import four.group.jahadi.Tests.Modules.SubModules.Visit;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DoctorSeeder {

    public static List<Module> seed() {

        return List.of(
                Module
                        .builder()
                        .tabName("پزشک")
                        .icon("icon-advice")
                        .isReferral(true)
                        .name("پزشک عمومی")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
//                                        MiniParaClinic.make(miniParaClinicId)
                                        MiniParaClinic.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .build(),
                Module
                        .builder()
                        .tabName("پزشک")
                        .icon("")
                        .isReferral(true)
                        .name("دورا پزشک")
                        .subModules(
                                List.of(
                                        Visit.make(),
                                        ExternalReferral.make(),
                                        MiniParaClinic.make()
                                )
                        )
                        .canSuggestDrug(true)
                        .canSuggestExperiment(true)
                        .build(),
                Module
                        .builder()
                        .tabName("پزشک")
                        .icon("")
                        .name("آموزش پس از پزشک")
                        .subModules(
                                List.of(
                                        SubModule
                                                .builder()
                                                .id(new ObjectId())
                                                .name("آموزش پس از پزشک")
                                                .questions(
                                                        List.of(
                                                                CheckListGroupQuestion
                                                                        .builder()
                                                                        .id(new ObjectId())
                                                                        .sectionTitle("غربالگری روان(سلامت)")
                                                                        .options(
                                                                                Arrays.stream(HaveOrNot.values()).map(haveOrNot ->
                                                                                        new PairValue(haveOrNot.name(), haveOrNot.getFaTranslate())
                                                                                ).collect(Collectors.toList())
                                                                        )
                                                                        .questionType(QuestionType.CHECK_LIST)
                                                                        .questions(List.of(
                                                                                SimpleQuestion
                                                                                        .builder()
                                                                                        .id(new ObjectId())
                                                                                        .questionType(QuestionType.SIMPLE)
                                                                                        .question("آموزش")
                                                                                        .answerType(AnswerType.TICK)
                                                                                        .required(false)
                                                                                        .build(),
                                                                                SimpleQuestion
                                                                                        .builder()
                                                                                        .id(new ObjectId())
                                                                                        .questionType(QuestionType.SIMPLE)
                                                                                        .question("بروشور")
                                                                                        .required(false)
                                                                                        .answerType(AnswerType.TICK)
                                                                                        .build()
                                                                        ))
                                                                        .build()
                                                        )
                                                )
                                                .build()
                                )
                        )
                        .build()
        );
    }

}
