package four.group.jahadi.Tests.Modules.SubModules.Gharbal;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Enums.Module.YesOrNo;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;

import java.util.List;

public class Mama {
    public static SubModule make() {
        return SubModule
                .builder()
                .name("غربال مامایی")
                .questions(List.of(
                        CheckListGroupQuestion
                                .builder()
                                .questionType(QuestionType.CHECK_LIST)
                                .sectionTitle("غربالگری سرطان پستان")
                                .options(List.of(
                                        new PairValue(
                                                YesOrNo.YES.name(),
                                                YesOrNo.YES.getFaTranslate()
                                        ),
                                        new PairValue(
                                                YesOrNo.NO.name(),
                                                YesOrNo.NO.getFaTranslate()
                                        )
                                ))
                                .questions(List.of(
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .question("سابقه ابتلا به سرطان در فرد، سابقه ابتلا به سرطان پستان، رحم یا تخمدان در خانواده")
                                                .answerType(AnswerType.TICK)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .question("داشتن علائم خطرناک ترشحات خونی، چرکی، شیر، وجود زخم و ...")
                                                .answerType(AnswerType.TICK)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .question("احساس توده توسط فرد")
                                                .answerType(AnswerType.TICK)
                                                .build()
                                ))
                                .build(),
                        CheckListGroupQuestion
                                .builder()
                                .questionType(QuestionType.CHECK_LIST)
                                .sectionTitle("قاعدگی")
                                .options(List.of(
                                        new PairValue(
                                                YesOrNo.YES.name(),
                                                YesOrNo.YES.getFaTranslate()
                                        ),
                                        new PairValue(
                                                YesOrNo.NO.name(),
                                                YesOrNo.NO.getFaTranslate()
                                        )
                                ))
                                .questions(List.of(
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .question("منارک و اختلالات بلوغ زودرس و دیررس - سن یائسگی")
                                                .answerType(AnswerType.TICK)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .question("نظم قاعدگی و بررسی الیگومنوره یا پلی منوره")
                                                .answerType(AnswerType.TICK)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .question("منوراژی، منومتروراژی، لکه بینی بعد از رابطه جنسی، لکه بینی پس از یائسگی")
                                                .answerType(AnswerType.TICK)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .question("PMS با علائم شدید یا دیس منوره")
                                                .answerType(AnswerType.TICK)
                                                .build()
                                ))
                                .build(),
                        CheckListGroupQuestion
                                .builder()
                                .questionType(QuestionType.CHECK_LIST)
                                .sectionTitle("حاملگی")
                                .options(List.of(
                                        new PairValue(
                                                YesOrNo.YES.name(),
                                                YesOrNo.YES.getFaTranslate()
                                        ),
                                        new PairValue(
                                                YesOrNo.NO.name(),
                                                YesOrNo.NO.getFaTranslate()
                                        )
                                ))
                                .questions(List.of(
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .question("مطمئن بودن از حاملگی (اگر مشکلی ندارد و تحت نظر است نیاز به ارجاع ندارد)")
                                                .answerType(AnswerType.TICK)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .question("مطمئن نبودن از حاملگی")
                                                .answerType(AnswerType.TICK)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .question("ناباروری یا نیاز به آموزش")
                                                .answerType(AnswerType.TICK)
                                                .build()
                                ))
                                .build(),
                        CheckListGroupQuestion
                                .builder()
                                .questionType(QuestionType.CHECK_LIST)
                                .sectionTitle("علائم خطرناک")
                                .options(List.of(
                                        new PairValue(
                                                YesOrNo.YES.name(),
                                                YesOrNo.YES.getFaTranslate()
                                        ),
                                        new PairValue(
                                                YesOrNo.NO.name(),
                                                YesOrNo.NO.getFaTranslate()
                                        )
                                ))
                                .questions(List.of(
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .question("درد لگن")
                                                .answerType(AnswerType.TICK)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .question("ترشحات غیرعادی(رنگ، بو، حجم)")
                                                .answerType(AnswerType.TICK)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .question("علائم STD در شریک جنسی")
                                                .answerType(AnswerType.TICK)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .question("لکه بینی")
                                                .answerType(AnswerType.TICK)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .question("خارش، تورم، قرمزی دستگاه تناسلی")
                                                .answerType(AnswerType.TICK)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .question("آزمایش یا سنوگرافی دارای مشکل خاص (با پزشک چک شود)")
                                                .answerType(AnswerType.TICK)
                                                .build()
                                ))
                                .build(),
                        SimpleQuestion
                                .builder()
                                .question("اختلال در رابطه جنسی")
                                .answerType(AnswerType.TICK)
                                .options(List.of(
                                        new PairValue(
                                                YesOrNo.YES.name(),
                                                YesOrNo.YES.getFaTranslate()
                                        ),
                                        new PairValue(
                                                YesOrNo.NO.name(),
                                                YesOrNo.NO.getFaTranslate()
                                        )
                                ))
                                .build()
                ))
                .build();
    }
}
