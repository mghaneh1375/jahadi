package four.group.jahadi.Tests.Modules.SubModules.Gharbal;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Enums.Module.YesOrNo;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Tests.Modules.SubModules.Helper;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.List;

public class Mama {
    public static SubModule make(String moduleName, ObjectId referToOid) {
        String subModuleName = "غربالگری مامایی";
        SubModule oldSubModule = Helper.findSubModule(moduleName, subModuleName);
        Question mainQuestion1 = Helper.findQuestionInSubModule(oldSubModule, "غربالگری سرطان پستان");
        Question mainQuestion2 = Helper.findQuestionInSubModule(oldSubModule, "قاعدگی");
        Question mainQuestion3 = Helper.findQuestionInSubModule(oldSubModule, "علائم خطرناک");
        Question mainQuestion4 = Helper.findQuestionInSubModule(oldSubModule, "اختلال در رابطه جنسی");

        return SubModule
                .builder()
                .id(oldSubModule == null ? new ObjectId() : oldSubModule.getId())
                .name(subModuleName)
                .referTo(referToOid)
                .isReferral(true)
                .questions(List.of(
                        CheckListGroupQuestion
                                .builder()
                                .id(mainQuestion1 == null ? new ObjectId() : mainQuestion1.getId())
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
                                                .id(Helper.findSimpleQuestionInList(
                                                        (CheckListGroupQuestion) mainQuestion1,
                                                        "سابقه ابتلا به سرطان در فرد، سابقه ابتلا به سرطان پستان، رحم یا تخمدان در خانواده"
                                                ))
                                                .questionType(QuestionType.SIMPLE)
                                                .question("سابقه ابتلا به سرطان در فرد، سابقه ابتلا به سرطان پستان، رحم یا تخمدان در خانواده")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList(
                                                        (CheckListGroupQuestion) mainQuestion1,
                                                        "داشتن علائم خطرناک ترشحات خونی، چرکی، شیر، وجود زخم و ..."
                                                ))
                                                .questionType(QuestionType.SIMPLE)
                                                .question("داشتن علائم خطرناک ترشحات خونی، چرکی، شیر، وجود زخم و ...")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList(
                                                        (CheckListGroupQuestion) mainQuestion1,
                                                        "احساس توده توسط فرد"
                                                ))
                                                .questionType(QuestionType.SIMPLE)
                                                .question("احساس توده توسط فرد")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build()
                                ))
                                .build(),
                        CheckListGroupQuestion
                                .builder()
                                .id(mainQuestion2 == null ? new ObjectId() : mainQuestion2.getId())
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
                                                .id(Helper.findSimpleQuestionInList(
                                                        (CheckListGroupQuestion) mainQuestion2,
                                                        "بلوغ زودرس یا دیررس"
                                                ))
                                                .questionType(QuestionType.SIMPLE)
                                                .question("بلوغ زودرس یا دیررس")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList(
                                                        (CheckListGroupQuestion) mainQuestion2,
                                                        "بی نظمی قاعدگی"
                                                ))
                                                .questionType(QuestionType.SIMPLE)
                                                .question("بی نظمی قاعدگی")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList(
                                                        (CheckListGroupQuestion) mainQuestion2,
                                                        "منوراژی، منومتروراژی، لکه بینی بعد از رابطه جنسی، لکه بینی پس از یائسگی"
                                                ))
                                                .questionType(QuestionType.SIMPLE)
                                                .question("منوراژی، منومتروراژی، لکه بینی بعد از رابطه جنسی، لکه بینی پس از یائسگی")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList(
                                                        (CheckListGroupQuestion) mainQuestion2,
                                                        "PMS با علائم شدید یا دیس منوره"
                                                ))
                                                .questionType(QuestionType.SIMPLE)
                                                .question("PMS با علائم شدید یا دیس منوره")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build()
                                ))
                                .build(),
                        CheckListGroupQuestion
                                .builder()
                                .id(mainQuestion3 == null ? new ObjectId() : mainQuestion3.getId())
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
                                                .id(Helper.findSimpleQuestionInList(
                                                        (CheckListGroupQuestion) mainQuestion3,
                                                        "درد لگن (حین و بعد رابطه)"
                                                ))
                                                .questionType(QuestionType.SIMPLE)
                                                .question("درد لگن (حین و بعد رابطه)")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList(
                                                        (CheckListGroupQuestion) mainQuestion3,
                                                        "ترشحات غیرعادی از نظر (رنگ، بو و حجم)"
                                                ))
                                                .questionType(QuestionType.SIMPLE)
                                                .question("ترشحات غیرعادی از نظر (رنگ، بو و حجم)")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList(
                                                        (CheckListGroupQuestion) mainQuestion3,
                                                        "علائم STD در شریک جنسی"
                                                ))
                                                .questionType(QuestionType.SIMPLE)
                                                .question("علائم STD در شریک جنسی")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList(
                                                        (CheckListGroupQuestion) mainQuestion3,
                                                        "لکه بینی"
                                                ))
                                                .questionType(QuestionType.SIMPLE)
                                                .question("لکه بینی")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList(
                                                        (CheckListGroupQuestion) mainQuestion3,
                                                        "خارش، تورم، قرمزی دستگاه تناسلی"
                                                ))
                                                .questionType(QuestionType.SIMPLE)
                                                .question("خارش، تورم، قرمزی دستگاه تناسلی")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList(
                                                        (CheckListGroupQuestion) mainQuestion3,
                                                        "آزمایش یا سنوگرافی دارای مشکل خاص"
                                                ))
                                                .questionType(QuestionType.SIMPLE)
                                                .question("آزمایش یا سنوگرافی دارای مشکل خاص")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build()
                                ))
                                .build(),
                        SimpleQuestion
                                .builder()
                                .id(mainQuestion4 == null ? new ObjectId() : mainQuestion4.getId())
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
