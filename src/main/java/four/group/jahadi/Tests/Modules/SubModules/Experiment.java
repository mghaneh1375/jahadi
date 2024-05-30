package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;

import java.util.List;

public class Experiment {

    public static SubModule make() {
        return SubModule
                .builder()
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .questionType(QuestionType.SIMPLE)
                                        .required(true)
                                        .question("علت درخواست")
                                        .answerType(AnswerType.LONG_TEXT)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .questionType(QuestionType.SIMPLE)
                                        .question("آزمایش")
                                        .required(false)
                                        .answerType(AnswerType.MULTI_SELECT)
                                        .dynamicOptions("experiments")
                                        .build()
                        )
                )
                .build();
    }

}
