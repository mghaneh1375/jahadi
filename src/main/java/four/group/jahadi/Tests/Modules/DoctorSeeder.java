package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.HaveOrNot;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Tests.Modules.SubModules.ExternalReferral;
import four.group.jahadi.Tests.Modules.SubModules.Helper;
import four.group.jahadi.Tests.Modules.SubModules.MiniParaClinic;
import four.group.jahadi.Tests.Modules.SubModules.Visit;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DoctorSeeder {

    public static List<Module> seed() {

        SubModule oldSubModule = Helper.findSubModule("آموزش پس از پزشک", "آموزش پس از پزشک");
        Question mainQuestion1 = Helper.findQuestionInSubModule(oldSubModule, "");

        return List.of(
                Module
                        .builder()
                        .tabName("پزشک")
                        .icon("icon-advice")
                        .isReferral(true)
                        .name("پزشک عمومی")
                        .subModules(
                                List.of(
                                        Visit.make("پزشک عمومی"),
                                        ExternalReferral.make("پزشک عمومی"),
//                                        MiniParaClinic.make(miniParaClinicId)
                                        MiniParaClinic.make("پزشک عمومی")
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
                                        Visit.make("دورا پزشک"),
                                        ExternalReferral.make("دورا پزشک"),
                                        MiniParaClinic.make("دورا پزشک")
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
                                                .id(oldSubModule == null ? new ObjectId() : oldSubModule.getId())
                                                .name("آموزش پس از پزشک")
                                                .questions(
                                                        List.of(
                                                                CheckListGroupQuestion
                                                                        .builder()
                                                                        .id(mainQuestion1 == null ? new ObjectId() : mainQuestion1.getId())
                                                                        .sectionTitle("")
                                                                        .options(
                                                                                Arrays.stream(HaveOrNot.values()).map(haveOrNot ->
                                                                                        new PairValue(haveOrNot.name(), haveOrNot.getFaTranslate())
                                                                                ).collect(Collectors.toList())
                                                                        )
                                                                        .questionType(QuestionType.CHECK_LIST)
                                                                        .questions(List.of(
                                                                                SimpleQuestion
                                                                                        .builder()
                                                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "آموزش"))
                                                                                        .questionType(QuestionType.SIMPLE)
                                                                                        .question("آموزش")
                                                                                        .answerType(AnswerType.TICK)
                                                                                        .required(false)
                                                                                        .build(),
                                                                                SimpleQuestion
                                                                                        .builder()
                                                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "بروشور"))
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
