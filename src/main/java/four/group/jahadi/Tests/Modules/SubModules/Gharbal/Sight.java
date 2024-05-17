package four.group.jahadi.Tests.Modules.SubModules.Gharbal;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.DiseaseBackground;
import four.group.jahadi.Enums.Module.YesOrNo;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;

import java.util.List;

public class Sight {
    public static SubModule make() {
        return SubModule
                .builder()
                .name("غربال بینایی")
                .questions(
                        List.of(
                                CheckListGroupQuestion
                                        .builder()
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
                                                        .question("بدون سابقه معاینه چشم")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("دارای سابقه تنبلی یا انحراف چشم")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("دارای عینک")
                                                        .answerType(AnswerType.TICK)
                                                        .build()
                                        ))
                                        .build(),
                                CheckListGroupQuestion
                                        .builder()
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
                                                        .question("دیابت، فشارخون")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("آب مروارید، گلوکوم")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("عینک نامناسب")
                                                        .answerType(AnswerType.TICK)
                                                        .build()
                                        ))
                                        .build(),
                                CheckListGroupQuestion
                                        .builder()
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
                                                        .question("انحراف چشم واضح => ناتوانی در انجام تست حرکت چشمی")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("پتوز، انتروپیون، اکتروپیون، آنیزوکوری، بسته نشدن کامل پلک ها")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("تاری دید (شغل و سن مهم) => پرسیدن و شدت تاری دید")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("دوبینی، نابینایی، لکه بینی در میدان دید، لکه سیاه در میدان دید، جرقه نورانی")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("قرمزی چشم، اشک ریزش (با پزشک چک شود)")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("درد چشمی (با پزشک چک شود)")
                                                        .answerType(AnswerType.TICK)
                                                        .build()
                                        ))
                                        .build()
                        )
                )
                .build();
    }
}
