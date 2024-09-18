package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.GroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import org.bson.types.ObjectId;

import java.util.List;

public class ExperimentAnswer {
    public static SubModule make() {
        return SubModule
                .builder()
                .id(new ObjectId())
                .isReferral(false)
                .name("جواب آزمایش ها")
                .questions(List.of(
                        GroupQuestion
                                .builder()
                                .id(new ObjectId())
                                .questionType(QuestionType.GROUP)
                                .sectionTitle("ارجاع به خارج")
                                .questions(List.of(
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.LONG_TEXT)
                                                .question("توضیحات")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.UPLOAD)
                                                .question("فایل مدنظر")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.LONG_TEXT)
                                                .question("توضیحات")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.UPLOAD)
                                                .question("فایل مدنظر")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.LONG_TEXT)
                                                .question("توضیحات")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.UPLOAD)
                                                .question("فایل مدنظر")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.LONG_TEXT)
                                                .question("توضیحات")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
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
