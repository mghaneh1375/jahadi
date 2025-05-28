package four.group.jahadi.Tests.Modules.SubModules.Gharbal;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Enums.Module.YesOrNo;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.List;

public class Mama {
    public static SubModule make(ObjectId referToOid) {
        return SubModule
                .builder()
                .id(new ObjectId())
                .name("غربالگری مامایی")
                .referTo(referToOid)
                .isReferral(true)
                .questions(List.of(
                        CheckListGroupQuestion
                                .builder()
                                .id(new ObjectId())
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
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("سابقه ابتلا به سرطان در فرد، سابقه ابتلا به سرطان پستان، رحم یا تخمدان در خانواده")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("داشتن علائم خطرناک ترشحات خونی، چرکی، شیر، وجود زخم و ...")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("احساس توده توسط فرد")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build()
                                ))
                                .build(),
                        CheckListGroupQuestion
                                .builder()
                                .id(new ObjectId())
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
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("بلوغ زودرس یا دیررس")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("بی نظمی قاعدگی")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("منوراژی، منومتروراژی، لکه بینی بعد از رابطه جنسی، لکه بینی پس از یائسگی")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("PMS با علائم شدید یا دیس منوره")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build()
                                ))
                                .build(),
                        CheckListGroupQuestion
                                .builder()
                                .id(new ObjectId())
                                .questionType(QuestionType.CHECK_LIST)
                                .sectionTitle("بارداری")
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
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("مطمئن بودن از بارداری")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("مشکوک به بارداری")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("ناباروری یا نیاز به آموزش")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("اشکال در باروری")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("خونریزی و لکه بینی")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("تروما به شکم یا غیره")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build()
                                ))
                                .build(),
                        CheckListGroupQuestion
                                .builder()
                                .id(new ObjectId())
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
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("درد لگن (حین و بعد رابطه)")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("ترشحات غیرعادی از نظر (رنگ، بو و حجم)")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("علائم STD در شریک جنسی")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("لکه بینی")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("خارش، تورم، قرمزی دستگاه تناسلی")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("آزمایش یا سنوگرافی دارای مشکل خاص")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build()
                                ))
                                .build(),
                        SimpleQuestion
                                .builder()
                                .id(new ObjectId())
                                .question("اختلال در رابطه جنسی")
                                .questionType(QuestionType.SIMPLE)
                                .answerType(AnswerType.TICK)
                                .required(false)
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
