package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import org.bson.types.ObjectId;

import java.util.List;

public class RemoteReferral {
    public static SubModule make() {
        return SubModule
                .builder()
                .id(new ObjectId())
                .name("دورا ارجاع")
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(true)
                                        .answerType(AnswerType.SELECT)
                                        .question("ارجاع به متخصصان دورا پزشک")
                                        .dynamicOptions("notInTripDoctors")
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(true)
                                        .answerType(AnswerType.LONG_TEXT)
                                        .question("شرح حال")
                                        .build()
                        )
                )
                .build();
    }
}
