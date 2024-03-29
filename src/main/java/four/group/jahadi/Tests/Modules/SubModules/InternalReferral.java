package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;

import java.util.List;

public class InternalReferral {
    public static SubModule make() {
        return SubModule
                .builder()
                .name("ارجاع داخلی")
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .required(true)
                                        .answerType(AnswerType.SELECT)
                                        .question("ارجاع به متخصصان داخلی")
                                        .dynamicOptions("inTripDoctors")
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .required(true)
                                        .answerType(AnswerType.LONG_TEXT)
                                        .question("شرح حال")
                                        .build()
                        )
                )
                .build();
    }
}
