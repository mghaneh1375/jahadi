package four.group.jahadi.Tests.Modules.SubModules.Sight;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import org.bson.types.ObjectId;

import java.util.List;

public class ExternalRefer {
    public static SubModule make() {
        return SubModule
                .builder()
                .id(new ObjectId())
                .name("ارجاع به مراکز درمانی متخصصان چشم")
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
                                        .question("علت ارجاع")
                                        .answerType(AnswerType.LONG_TEXT)
                                        .build()
                        )
                )
                .build();
    }
}
