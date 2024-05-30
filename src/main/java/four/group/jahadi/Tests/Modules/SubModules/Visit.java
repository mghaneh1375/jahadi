package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;

import java.util.List;

public class Visit {

    public static SubModule make() {

        return SubModule.builder()
                .questions(List.of(
                        SimpleQuestion
                                .builder()
                                .questionType(QuestionType.SIMPLE)
                                .question("توضیحات")
                                .required(true)
                                .answerType(AnswerType.LONG_TEXT)
                                .build(),
                        SimpleQuestion
                                .builder()
                                .questionType(QuestionType.SIMPLE)
                                .question("دارو")
                                .required(false)
                                .answerType(AnswerType.MULTI_SELECT)
                                .dynamicOptions("drugs")
                                .build()
                ))
                .build();

    }

}
