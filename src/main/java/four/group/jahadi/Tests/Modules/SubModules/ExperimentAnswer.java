package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.GroupQuestion;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import org.bson.types.ObjectId;

import java.util.List;

public class ExperimentAnswer {
    public static SubModule make(String moduleName) {
        String subModuleName = "جواب آزمایش ها";
        SubModule oldSubModule = Helper.findSubModule(moduleName, subModuleName);
        Question mainQuestion1 = Helper.findQuestionInSubModule(oldSubModule, "ارجاع به مراکز درمانی");
        ObjectId question11 = mainQuestion1 == null || ((GroupQuestion)mainQuestion1).getQuestions().size() < 1 ? new ObjectId() : ((GroupQuestion)mainQuestion1).getQuestions().get(0).getId();
        ObjectId question12 = mainQuestion1 == null || ((GroupQuestion)mainQuestion1).getQuestions().size() < 2 ? new ObjectId() : ((GroupQuestion)mainQuestion1).getQuestions().get(1).getId();
        ObjectId question13 = mainQuestion1 == null || ((GroupQuestion)mainQuestion1).getQuestions().size() < 3 ? new ObjectId() : ((GroupQuestion)mainQuestion1).getQuestions().get(2).getId();
        ObjectId question14 = mainQuestion1 == null || ((GroupQuestion)mainQuestion1).getQuestions().size() < 4 ? new ObjectId() : ((GroupQuestion)mainQuestion1).getQuestions().get(3).getId();
        ObjectId question15 = mainQuestion1 == null || ((GroupQuestion)mainQuestion1).getQuestions().size() < 5 ? new ObjectId() : ((GroupQuestion)mainQuestion1).getQuestions().get(4).getId();
        ObjectId question16 = mainQuestion1 == null || ((GroupQuestion)mainQuestion1).getQuestions().size() < 6 ? new ObjectId() : ((GroupQuestion)mainQuestion1).getQuestions().get(5).getId();
        ObjectId question17 = mainQuestion1 == null || ((GroupQuestion)mainQuestion1).getQuestions().size() < 7 ? new ObjectId() : ((GroupQuestion)mainQuestion1).getQuestions().get(6).getId();

        return SubModule
                .builder()
                .id(oldSubModule == null ? new ObjectId() : oldSubModule.getId())
                .name(subModuleName)
                .isReferral(false)
                .questions(List.of(
                        GroupQuestion
                                .builder()
                                .id(mainQuestion1 == null ? new ObjectId() : mainQuestion1.getId())
                                .questionType(QuestionType.GROUP)
                                .sectionTitle("ارجاع به مراکز درمانی")
                                .questions(List.of(
                                        SimpleQuestion
                                                .builder()
                                                .id(question11)
                                                .required(false)
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.LONG_TEXT)
                                                .question("توضیحات")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(question12)
                                                .required(false)
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.UPLOAD)
                                                .question("فایل مدنظر")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(question13)
                                                .required(false)
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.LONG_TEXT)
                                                .question("توضیحات")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(question14)
                                                .required(false)
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.UPLOAD)
                                                .question("فایل مدنظر")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(question15)
                                                .required(false)
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.LONG_TEXT)
                                                .question("توضیحات")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(question15)
                                                .required(false)
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.UPLOAD)
                                                .question("فایل مدنظر")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(question16)
                                                .required(false)
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.LONG_TEXT)
                                                .question("توضیحات")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(question17)
                                                .required(false)
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.UPLOAD)
                                                .question("فایل مدنظر")
                                                .build()
                                ))
                                .build()
                ))
                .build();
    }
}
