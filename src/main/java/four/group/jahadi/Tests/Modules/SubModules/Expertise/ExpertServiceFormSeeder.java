package four.group.jahadi.Tests.Modules.SubModules.Expertise;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Tests.Modules.SubModules.Helper;
import org.bson.types.ObjectId;

import java.util.List;

public class ExpertServiceFormSeeder {
    public static SubModule make(String moduleName) {
        String subModuleName = "خدمت";
        SubModule oldSubModule = Helper.findSubModule(moduleName, subModuleName);
        Question mainQuestion1 = Helper.findQuestionInSubModule(oldSubModule, "نوع خدمت");
        Question mainQuestion2 = Helper.findQuestionInSubModule(oldSubModule, "توضیحات");
        Question mainQuestion3 = Helper.findQuestionInSubModule(oldSubModule, "گزارش خدمت");
        Question mainQuestion4 = Helper.findQuestionInSubModule(oldSubModule, "فایل موردنظر خود را بارگذاری فرمایید");

        return SubModule
                .builder()
                .id(oldSubModule == null ? new ObjectId() : oldSubModule.getId())
                .name(subModuleName)
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion1 == null ? new ObjectId() : mainQuestion1.getId())
                                        .question("نوع خدمت")
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .options(List.of())
                                        .answerType(AnswerType.SELECT)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion2 == null ? new ObjectId() : mainQuestion2.getId())
                                        .question("توضیحات")
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .answerType(AnswerType.LONG_TEXT)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion3 == null ? new ObjectId() : mainQuestion3.getId())
                                        .question("گزارش خدمت")
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .answerType(AnswerType.LONG_TEXT)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion4 == null ? new ObjectId() : mainQuestion4.getId())
                                        .question("فایل موردنظر خود را بارگذاری فرمایید")
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .answerType(AnswerType.UPLOAD)
                                        .build()
                        )
                )
                .build();
    }
}
