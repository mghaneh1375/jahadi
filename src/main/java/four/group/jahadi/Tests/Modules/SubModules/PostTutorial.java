package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.HaveOrNot;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PostTutorial {
    public static SubModule make() {
        return SubModule
                .builder()
                .id(new ObjectId())
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
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("بروشور")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build()
                                        ))
                                        .build()
                        )
                )
                .build();
    }
}
