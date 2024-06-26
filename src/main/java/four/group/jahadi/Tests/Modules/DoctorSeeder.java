package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.HaveOrNot;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static four.group.jahadi.Tests.Modules.ModuleSeeder.commonSubModules;

public class DoctorSeeder {

    public static List<Module> seed() {

        return List.of(
                Module
                        .builder()
                        .tabName("پزشک")
                        .icon("")
                        .isReferral(true)
                        .name("پزشک عمومی")
                        .subModules(
                                List.of(
                                        commonSubModules.get("visit"),
//                                        commonSubModules.get("drug"),
                                        commonSubModules.get("externalReferral"),
                                        commonSubModules.get("paraClinic")
//                                        commonSubModules.get("experiment"),
                                )
                        )
                        .build(),
                Module
                        .builder()
                        .tabName("پزشک")
                        .icon("")
                        .isReferral(true)
                        .name("دورا پزشک")
                        .subModules(
                                List.of(
                                        commonSubModules.get("visit"),
//                                        commonSubModules.get("drug"),
                                        commonSubModules.get("externalReferral"),
                                        commonSubModules.get("paraClinic")
//                                        commonSubModules.get("experiment"),
                                )
                        )
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
                                                .name("آموزش پس از پزشک")
                                                .questions(
                                                        List.of(
                                                                CheckListGroupQuestion
                                                                        .builder()
                                                                        .id(new ObjectId())
                                                                        .sectionTitle("غربال روان(سلامت)")
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
                                                                                        .build(),
                                                                                SimpleQuestion
                                                                                        .builder()
                                                                                        .id(new ObjectId())
                                                                                        .questionType(QuestionType.SIMPLE)
                                                                                        .question("بروشور")
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
