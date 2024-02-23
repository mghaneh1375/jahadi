package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;

import java.util.List;

public class ExternalReferral {
    public static SubModule make() {
        return SubModule
                .builder()
                .name("ارجاع به خارج")
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .required(true)
                                        .answerType(AnswerType.TEXT)
                                        .question("ارجاع به")
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .required(true)
                                        .answerType(AnswerType.LONG_TEXT)
                                        .question("علت ارجاع")
                                        .build()
                                )
                )
                .build();
    }
}
