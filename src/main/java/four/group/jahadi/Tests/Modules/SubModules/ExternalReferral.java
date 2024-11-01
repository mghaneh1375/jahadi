package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import org.bson.types.ObjectId;

import java.util.List;

public class ExternalReferral {
    public static SubModule make() {
        return SubModule
                .builder()
                .id(new ObjectId())
                .name("ارجاع به خارج")
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .answerType(AnswerType.TEXT)
                                        .question("ارجاع به")
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .answerType(AnswerType.LONG_TEXT)
                                        .question("علت ارجاع")
                                        .build()
                                )
                )
                .build();
    }
}
