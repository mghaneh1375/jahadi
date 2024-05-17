package four.group.jahadi.Tests.Modules.SubModules.Gharbal;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.DiseaseBackground;
import four.group.jahadi.Enums.Module.YesOrNo;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;

import java.util.List;

public class Audiologists {
    public static SubModule make() {
        return SubModule
                .builder()
                .name("غربال شنوایی")
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
                                                        .question("بدون سابقه شنوایی سنجی")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("سابقه کم شنوایی یا ناشنوایی در خانواده")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("سابقه بستری در بیمارستان زمان نوزادی و زردی و ازدواج فامیلی والدین")
                                                        .answerType(AnswerType.TICK)
                                                        .build()
                                        ))
                                        .build(),
                                CheckListGroupQuestion
                                        .builder()
                                        .sectionTitle("افراد مشکوک")
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
                                                        .question("احساس کم شنوایی (زیر 40 سال)")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("دارای سمعک نامناسب")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("دارای احساس گرفتگی گوش (با پزشک چک شود)")
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
                                                        .question("کاهش شنوایی یک طرفه یا دو طرفه پیش رونده یا شدید ثابت")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("احساس وزوز یا سکوت کشیدن در گوش")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("سرگیجه دورانی (با پزشک چک شود)")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("تروما یا دردگوش (با پزشک چک شود)")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("ترشح از گوش (با پزشک چک شود)")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("کاهش شنوایی ناگهانی، کاهش شنوایی مربوط به شغل یا تاثیرگذار در خلق و شغل")
                                                        .answerType(AnswerType.TICK)
                                                        .build()
                                        ))
                                        .build()
                        )
                )
                .build();
    }
}
