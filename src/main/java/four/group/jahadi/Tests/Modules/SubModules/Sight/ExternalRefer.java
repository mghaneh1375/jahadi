package four.group.jahadi.Tests.Modules.SubModules.Sight;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Tests.Modules.SubModules.Helper;
import org.bson.types.ObjectId;

import java.util.List;

public class ExternalRefer {
    public static SubModule make(String moduleName) {
        String subModuleName = "ارجاع به مراکز درمانی متخصصان چشم";
        SubModule oldSubModule = Helper.findSubModule(moduleName, subModuleName);
        Question question1 = Helper.findQuestionInSubModule(oldSubModule, "ارجاع به");
        Question question2 = Helper.findQuestionInSubModule(oldSubModule, "علت ارجاع");

        return SubModule
                .builder()
                .id(oldSubModule == null ? new ObjectId() : oldSubModule.getId())
                .name(subModuleName)
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .id(question1 == null ? new ObjectId() : question1.getId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .answerType(AnswerType.TEXT)
                                        .question("ارجاع به")
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(question2 == null ? new ObjectId() : question2.getId())
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
