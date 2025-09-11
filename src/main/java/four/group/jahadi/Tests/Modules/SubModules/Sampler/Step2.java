package four.group.jahadi.Tests.Modules.SubModules.Sampler;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.GroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.Question.TableQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.List;

public class Step2 {
    public static SubModule make() {
        return SubModule
                .builder()
                .id(new ObjectId())
                .name("فرم نتیجه آزمایش ادرار")
                .isReferral(false)
                .questions(
                        List.of(
                                GroupQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.GROUP)
                                        .sectionTitle("نتیجه آزمایش ادرار")
                                        .questions(List.of(
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("تست Blood (60 ثانیه)")
                                                        .required(false)
                                                        .answerType(AnswerType.SELECT)
                                                        .options(
                                                                List.of(
                                                                        new PairValue("-", "-"),
                                                                        new PairValue("+", "+"),
                                                                        new PairValue("++", "++"),
                                                                        new PairValue("+++", "+++"),
                                                                        new PairValue("10", "10"),
                                                                        new PairValue("50", "50"),
                                                                        new PairValue("250", "250")
                                                                )
                                                        )
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .required(false)
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("تست Nitrite (60 ثانیه)")
                                                        .answerType(AnswerType.SELECT)
                                                        .options(
                                                                List.of(
                                                                        new PairValue("-", "-"),
                                                                        new PairValue("+", "+")
                                                                )
                                                        )
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .required(false)
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("تست Leukocytes (120 ثانیه)")
                                                        .answerType(AnswerType.SELECT)
                                                        .options(
                                                                List.of(
                                                                        new PairValue("-", "-"),
                                                                        new PairValue("25", "25"),
                                                                        new PairValue("75", "75"),
                                                                        new PairValue("500", "500")
                                                                )
                                                        )
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .required(false)
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("تست Protein (30 ثانیه)")
                                                        .answerType(AnswerType.SELECT)
                                                        .options(
                                                                List.of(
                                                                        new PairValue("-", "-"),
                                                                        new PairValue("+(30)", "+(30)"),
                                                                        new PairValue("++(100)", "++(100)"),
                                                                        new PairValue("+++(500)", "+++(500)")
                                                                )
                                                        )
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .required(false)
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("تست Ketone (90 ثانیه)")
                                                        .answerType(AnswerType.SELECT)
                                                        .options(
                                                                List.of(
                                                                        new PairValue("-", "-"),
                                                                        new PairValue("trace(5)", "trace(5)"),
                                                                        new PairValue("+(15)", "+(15)"),
                                                                        new PairValue("++(40)", "++(40)"),
                                                                        new PairValue("+++(80)", "+++(80)")
                                                                )
                                                        )
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .required(false)
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("تست Glucose (90 ثانیه)")
                                                        .answerType(AnswerType.SELECT)
                                                        .options(
                                                                List.of(
                                                                        new PairValue("-", "-"),
                                                                        new PairValue("50", "50"),
                                                                        new PairValue("100", "100"),
                                                                        new PairValue("250", "250"),
                                                                        new PairValue("500", "500")
                                                                )
                                                        )
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .required(false)
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("تست Uroblilinogen (90 ثانیه)")
                                                        .answerType(AnswerType.SELECT)
                                                        .options(
                                                                List.of(
                                                                        new PairValue("normal", "normal"),
                                                                        new PairValue("trace", "trace"),
                                                                        new PairValue("+", "+"),
                                                                        new PairValue("++", "++"),
                                                                        new PairValue("+++", "+++")
                                                                )
                                                        )
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .required(false)
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("تست Bilirubin (60 ثانیه)")
                                                        .answerType(AnswerType.SELECT)
                                                        .options(
                                                                List.of(
                                                                        new PairValue("-", "-"),
                                                                        new PairValue("+(1)", "+(1)"),
                                                                        new PairValue("++(2)", "++(2)"),
                                                                        new PairValue("+++(4)", "+++(4)")
                                                                )
                                                        )
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .required(false)
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("تست Ascorbic Acid (30 ثانیه)")
                                                        .answerType(AnswerType.SELECT)
                                                        .options(
                                                                List.of(
                                                                        new PairValue("-", "-"),
                                                                        new PairValue("10", "10"),
                                                                        new PairValue("25", "25")
                                                                )
                                                        )
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .required(false)
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("تست PH (30 ثانیه)")
                                                        .answerType(AnswerType.SELECT)
                                                        .options(
                                                                List.of(
                                                                        new PairValue("5", "5"),
                                                                        new PairValue("6", "6"),
                                                                        new PairValue("6.5", "6.5"),
                                                                        new PairValue("7", "7"),
                                                                        new PairValue("8", "8"),
                                                                        new PairValue("9", "9")
                                                                )
                                                        )
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .required(false)
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("تست Specific Gravity (60 ثانیه)")
                                                        .answerType(AnswerType.SELECT)
                                                        .options(
                                                                List.of(
                                                                        new PairValue("1.000", "1.000"),
                                                                        new PairValue("1.005", "1.005"),
                                                                        new PairValue("1.010", "1.010"),
                                                                        new PairValue("1.015", "1.015"),
                                                                        new PairValue("1.020", "1.020"),
                                                                        new PairValue("1.025", "1.025"),
                                                                        new PairValue("1.030", "1.030")
                                                                )
                                                        )
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .required(false)
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("تست موارد شفافیت")
                                                        .answerType(AnswerType.SELECT)
                                                        .options(
                                                                List.of(
                                                                        new PairValue("transparent", "شفاف"),
                                                                        new PairValue("opaque", "کدر")
                                                                )
                                                        )
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .required(false)
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("بوی نامطبوع")
                                                        .answerType(AnswerType.SELECT)
                                                        .options(
                                                                List.of(
                                                                        new PairValue("have", "دارد"),
                                                                        new PairValue("not_have", "ندارد")
                                                                )
                                                        )
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .required(false)
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("رنگ")
                                                        .answerType(AnswerType.TEXT)
                                                        .build()
                                        ))
                                        .build(),
                                TableQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .required(false)
                                        .title("جدول")
                                        .questionType(QuestionType.TABLE)
                                        .headers(List.of("Beta HCG", "Result"))
                                        .firstColumn(List.of("Positive", "Indefinite", "Specimen", "Negative"))
                                        .rowsCount(4)
                                        .answerType(AnswerType.TEXT)
                                        .build()
                        )
                )
                .build();
    }
}
