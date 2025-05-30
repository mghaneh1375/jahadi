package four.group.jahadi.Tests.Modules.SubModules.Gharbal;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.DiseaseBackground;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Enums.Module.YesOrNo;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.List;

public class Audiologists {
    public static SubModule make(ObjectId referToOid) {
        return SubModule
                .builder()
                .id(new ObjectId())
                .name("غربالگری شنوایی")
                .referTo(referToOid)
                .isReferral(true)
                .questions(
                        List.of(
                                CheckListGroupQuestion
                                        .builder()
                                        .id(new ObjectId())
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
                                                        .id(new ObjectId())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("بدون سابقه شنوایی سنجی")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("سابقه کم شنوایی یا ناشنوایی در خانواده")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("سابقه بستری در بیمارستان زمان نوزادی و زردی و ازدواج فامیلی والدین")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build()
                                        ))
                                        .build(),
                                CheckListGroupQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.CHECK_LIST)
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
                                                        .id(new ObjectId())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("احساس کم شنوایی")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("دارای سمعک نامناسب")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("دارای احساس گرفتگی گوش")
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
                                                        .id(new ObjectId())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("کاهش شنوایی یک طرفه یا دو طرفه پیش رونده یا شدید ثابت")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("احساس وزوز یا سوت کشیدن در گوش")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("سرگیجه دورانی")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("تروما یا دردگوش")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("ترشح از گوش")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("کاهش شنوایی ناگهانی، کاهش شنوایی مربوط به شغل یا تاثیرگذار در خلق و شغل")
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
