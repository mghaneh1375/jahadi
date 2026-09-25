package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import org.bson.types.ObjectId;

import java.util.List;

public class ExternalReferral {
    public static SubModule make(String moduleName) {
        String subModuleName = "ارجاع به مراکز درمانی";
        SubModule oldSubModule = Helper.findSubModule(moduleName, subModuleName);
        Question mainQuestion1 = Helper.findQuestionInSubModule(oldSubModule, "ارجاع به");
        Question mainQuestion2 = Helper.findQuestionInSubModule(oldSubModule, "علت ارجاع");

        return SubModule
                .builder()
                .id(oldSubModule == null ? new ObjectId() : oldSubModule.getId())
                .name(subModuleName)
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion1 == null ? new ObjectId() : mainQuestion1.getId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .answerType(AnswerType.TEXT)
                                        .question("ارجاع به")
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion2 == null ? new ObjectId() : mainQuestion2.getId())
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
