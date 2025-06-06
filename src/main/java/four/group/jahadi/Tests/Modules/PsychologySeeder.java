package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Enums.Module.AnswerType;
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

import static four.group.jahadi.Tests.Modules.ModuleSeeder.moduleIds;

public class PsychologySeeder {
    public static Module seed() {
        return Module
                .builder()
                .name("روانشناس")
                .tabName("توان بخشی")
                .icon("")
                .isReferral(false)
                .subModules(
                        List.of(
                                SubModule
                                        .builder()
                                        .id(new ObjectId())
                                        .referTo(moduleIds.get("متخصص روان"))
                                        .name("خدمات روان شناس")
                                        .questions(
                                                List.of(
                                                        CheckListGroupQuestion
                                                                .builder()
                                                                .id(new ObjectId())
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
                                                                                        .id(new ObjectId())
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
