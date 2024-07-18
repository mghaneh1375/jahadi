package four.group.jahadi.Tests.Modules.SubModules.Expertise;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import org.bson.types.ObjectId;

import java.util.List;

public class WomenServiceSeeder {
    public static SubModule make() {
        return SubModule
                .builder()
                .id(new ObjectId())
                .name("خدمت")
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .question("نوع خدمت")
                                        .questionType(QuestionType.SIMPLE)
                                        .required(true)
                                        .options(List.of())
                                        .answerType(AnswerType.SELECT)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .question("توضیحات")
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .answerType(AnswerType.LONG_TEXT)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .question("گزارش خدمت")
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .answerType(AnswerType.LONG_TEXT)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
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
