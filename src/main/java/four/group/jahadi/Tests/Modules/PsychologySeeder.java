package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Tests.Modules.SubModules.Helper;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static four.group.jahadi.Tests.Modules.ModuleSeeder.moduleIds;

public class PsychologySeeder {
    public static Module seed() {
        String moduleName = "روانشناس";
        String subModuleName = "خدمات روان شناس";
        SubModule oldSubModule = Helper.findSubModule(moduleName, subModuleName);
        Question mainQuestion1 = Helper.findQuestionInSubModule(oldSubModule, "خدمات روان شناس");

        return Module
                .builder()
                .name(moduleName)
                .tabName("توان بخشی")
                .icon("")
                .isReferral(false)
                .subModules(
                        List.of(
                                SubModule
                                        .builder()
                                        .id(oldSubModule == null ? new ObjectId() : oldSubModule.getId())
                                        .name(subModuleName)
                                        .referTo(moduleIds.get("متخصص روان"))
                                        .questions(
                                                List.of(
                                                        CheckListGroupQuestion
                                                                .builder()
                                                                .id(mainQuestion1 == null ? new ObjectId() : mainQuestion1.getId())
                                                                .questionType(QuestionType.CHECK_LIST)
                                                                .sectionTitle("خدمات روان شناس")
                                                                .options(
                                                                        Arrays.stream(four.group.jahadi.Enums.Module.DoneOrNot.values())
                                                                                .map(itr -> new PairValue(
                                                                                        itr.name(),
                                                                                        itr.getFaTranslate()
                                                                                ))
                                                                                .collect(Collectors.toList())
                                                                )
                                                                .questions(
                                                                        List.of(
                                                                                SimpleQuestion
                                                                                        .builder()
                                                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "مشاوره"))
                                                                                        .questionType(QuestionType.SIMPLE)
                                                                                        .question("مشاوره")
                                                                                        .answerType(AnswerType.TICK)
                                                                                        .required(false)
                                                                                        .canWriteDesc(true)
                                                                                        .build()
                                                                        )
                                                                )
                                                                .canWriteTime(true)
                                                                .canWriteDesc(true)
                                                                .build()
                                                )
                                        )
                                        .build()
                        )
                )
                .build();
    }
}
