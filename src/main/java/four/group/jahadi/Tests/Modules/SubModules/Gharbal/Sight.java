package four.group.jahadi.Tests.Modules.SubModules.Gharbal;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.DiseaseBackground;
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

public class Sight {
    public static SubModule make(String moduleName, ObjectId referToOid) {
        String subModuleName = "غربالگری بینایی";
        SubModule oldSubModule = Helper.findSubModule(moduleName, subModuleName);
        Question mainQuestion1 = Helper.findQuestionInSubModule(oldSubModule, "کودکان");
        Question mainQuestion2 = Helper.findQuestionInSubModule(oldSubModule, "بیماری زمینه ای");
        Question mainQuestion3 = Helper.findQuestionInSubModule(oldSubModule, "علائم خطرناک");

        return SubModule
                .builder()
                .id(oldSubModule == null ? new ObjectId() : oldSubModule.getId())
                .name(subModuleName)
                .referTo(referToOid)
                .isReferral(true)
                .questions(
                        List.of(
                                CheckListGroupQuestion
                                        .builder()
                                        .id(mainQuestion1 == null ? new ObjectId() : mainQuestion1.getId())
                                        .questionType(QuestionType.CHECK_LIST)
                                        .sectionTitle("کودکان")
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
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "بدون سابقه معاینه چشم"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("بدون سابقه معاینه چشم")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "دارای سابقه تنبلی یا انحراف چشم"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("دارای سابقه تنبلی یا انحراف چشم")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "دارای عینک"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("دارای عینک")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build()
                                        ))
                                        .build(),
                                CheckListGroupQuestion
                                        .builder()
                                        .id(mainQuestion2 == null ? new ObjectId() : mainQuestion2.getId())
                                        .questionType(QuestionType.CHECK_LIST)
                                        .sectionTitle("بیماری زمینه ای")
                                        .options(List.of(
                                                new PairValue(
                                                        DiseaseBackground.HAS_DISEASE_BACKGROUND.name(),
                                                        DiseaseBackground.HAS_DISEASE_BACKGROUND.getFaTranslate()
                                                ),
                                                new PairValue(
                                                        DiseaseBackground.HAS_NOT_DISEASE_BACKGROUND.name(),
                                                        DiseaseBackground.HAS_NOT_DISEASE_BACKGROUND.getFaTranslate()
                                                )
                                        ))
                                        .questions(List.of(
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion2, "دیابت، فشارخون"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("دیابت، فشارخون")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion2, "آب مروارید، گلوکوم"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("آب مروارید، گلوکوم")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion2, "عینک نامناسب"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("عینک نامناسب")
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
                                                        DiseaseBackground.HAS_DISEASE_BACKGROUND.name(),
                                                        DiseaseBackground.HAS_DISEASE_BACKGROUND.getFaTranslate()
                                                ),
                                                new PairValue(
                                                        DiseaseBackground.HAS_NOT_DISEASE_BACKGROUND.name(),
                                                        DiseaseBackground.HAS_NOT_DISEASE_BACKGROUND.getFaTranslate()
                                                )
                                        ))
                                        .questions(List.of(
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion3, "انحراف چشم واضح => ناتوانی در انجام تست حرکت چشمی"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("انحراف چشم واضح => ناتوانی در انجام تست حرکت چشمی")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion3, "پتوز، انتروپیون، اکتروپیون، آنیزوکوری، بسته نشدن کامل پلک ها"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("پتوز، انتروپیون، اکتروپیون، آنیزوکوری، بسته نشدن کامل پلک ها")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion3, "تاری دید نزدیک => پرسیدن و شدت تاری دید"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("تاری دید نزدیک => پرسیدن و شدت تاری دید")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion3, "تاری دید دور"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("تاری دید دور")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion3, "دوبینی، نابینایی، لکه بینی در میدان دید، لکه سیاه در میدان دید، جرقه نورانی"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("دوبینی، نابینایی، لکه بینی در میدان دید، لکه سیاه در میدان دید، جرقه نورانی")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion3, "قرمزی چشم، اشک ریزش، سوزش و خارش (با پزشک چک شود)"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("قرمزی چشم، اشک ریزش، سوزش و خارش (با پزشک چک شود)")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion3, "درد چشمی (با پزشک چک شود)"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("درد چشمی (با پزشک چک شود)")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion3, "جسم خارجی در چشم"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("جسم خارجی در چشم")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion3, "تروما به چشم"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("تروما به چشم")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build()
                                        ))
                                        .build()
                        )
                )
                .build();
    }
}
