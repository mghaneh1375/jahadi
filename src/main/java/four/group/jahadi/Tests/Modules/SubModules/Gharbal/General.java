package four.group.jahadi.Tests.Modules.SubModules.Gharbal;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.DiseaseBackground;
import four.group.jahadi.Enums.Module.DrugBackground;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;

import java.util.List;

public class General {
    public static SubModule make() {
        return SubModule
                .builder()
                .name("غربالگری کلی")
                .questions(
                        List.of(
                                CheckListGroupQuestion
                                        .builder()
                                        .sectionTitle("سابقه بیماری")
                                        .options(List.of(
                                                new PairValue(
                                                        DiseaseBackground.HAS_DISEASE_BACKGROUND.name(),
                                                        DiseaseBackground.HAS_DISEASE_BACKGROUND.getFaTranslate()
                                                ),
                                                new PairValue(
                                                        DiseaseBackground.HAS_NOT_DISEASE_BACKGROUND.name(),
                                                        DiseaseBackground.HAS_NOT_DISEASE_BACKGROUND.getFaTranslate()
                                                ),
                                                new PairValue(
                                                        DrugBackground.HAS_DRUG_BACKGROUND.name(),
                                                        DrugBackground.HAS_DRUG_BACKGROUND.getFaTranslate()
                                                ),
                                                new PairValue(
                                                        DrugBackground.HAS_NOT_DRUG_BACKGROUND.name(),
                                                        DrugBackground.HAS_NOT_DRUG_BACKGROUND.getFaTranslate()
                                                )
                                        ))
                                        .questions(List.of(
                                                SimpleQuestion
                                                        .builder()
                                                        .question("دیابت ملیتوس (DM)")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("پرفشاری خون (HTN)")
                                                        .answerType(AnswerType.TICK)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .question("مشکل تیروئید")
                                                        .answerType(AnswerType.TICK)
                                                        .build()
                                        ))
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .required(true)
                                        .question("قند خون ناشتا (FBS)")
                                        .answerType(AnswerType.NUMBER)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .required(true)
                                        .question("قند خون غیرناشتا (BS)")
                                        .answerType(AnswerType.NUMBER)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .required(true)
                                        .question("فشار خون (BP)")
                                        .answerType(AnswerType.NUMBER)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .required(true)
                                        .question("فشار خون اول (First BP)")
                                        .answerType(AnswerType.NUMBER)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .required(true)
                                        .question("دارو (Medicine)")
                                        .answerType(AnswerType.TEXT)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .required(true)
                                        .question("فشار خون دوم (Second BP)")
                                        .answerType(AnswerType.NUMBER)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .required(true)
                                        .question("دارو بار دوم (Medicine 2)")
                                        .answerType(AnswerType.TEXT)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .required(true)
                                        .question("فشار خون سوم (Third BP)")
                                        .answerType(AnswerType.NUMBER)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .required(false)
                                        .question("توضیحات")
                                        .answerType(AnswerType.LONG_TEXT)
                                        .build()
                        )
                )
                .build();
    }
}
