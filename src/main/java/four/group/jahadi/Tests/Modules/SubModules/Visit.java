package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import org.bson.types.ObjectId;

import java.util.List;

public class Visit {

    public static SubModule make(String moduleName) {
        String subModuleName = "ویزیت و تشخیص";
        SubModule oldSubModule = Helper.findSubModule(moduleName, subModuleName);
        Question mainQuestion1 = Helper.findQuestionInSubModule(oldSubModule, "توضیحات");

        return SubModule.builder()
                .id(oldSubModule == null ? new ObjectId() : oldSubModule.getId())
                .name(subModuleName)
                .questions(List.of(
                        SimpleQuestion
                                .builder()
                                .id(mainQuestion1 == null ? new ObjectId() : mainQuestion1.getId())
                                .questionType(QuestionType.SIMPLE)
                                .question("توضیحات")
                                .required(false)
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
