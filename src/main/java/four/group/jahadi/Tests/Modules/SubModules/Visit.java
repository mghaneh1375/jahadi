package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import org.bson.types.ObjectId;

import java.util.List;

public class Visit {

    public static SubModule make() {
        return SubModule.builder()
                .id(new ObjectId())
                .name("ویزیت و تشخیص")
                .questions(List.of(
                        SimpleQuestion
                                .builder()
                                .id(new ObjectId())
                                .questionType(QuestionType.SIMPLE)
                                .question("توضیحات")
                                .required(true)
                                .answerType(AnswerType.LONG_TEXT)
                                .build()
//                        SimpleQuestion
//                                .builder()
//                                .id(new ObjectId())
//                                .questionType(QuestionType.SIMPLE)
//                                .question("دارو")
//                                .required(false)
//                                .answerType(AnswerType.MULTI_SELECT)
//                                .dynamicOptions("drugs")
//                                .build()
                ))
                .build();

    }

}
