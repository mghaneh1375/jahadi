package four.group.jahadi.Tests.Modules.SubModules.Gharbal;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.DiseaseBackground;
import four.group.jahadi.Enums.Module.DrugBackground;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.List;

public class General {
    public static SubModule make(ObjectId referToOid) {
        return SubModule
                .builder()
                .id(new ObjectId())
                .name("غربالگری کلی")
                .referTo(referToOid)
                .isReferral(true)
                .questions(
                        List.of(
                                CheckListGroupQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.CHECK_LIST)
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
                                                        .id(new ObjectId())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("دیابت ملیتوس (DM)")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("پرفشاری خون (HTN)")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("مشکل تیروئید")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build()
                                        ))
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .question("قند خون ناشتا (FBS)")
                                        .answerType(AnswerType.NUMBER)
//                                        .canWriteDesc(true)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .question("قند خون غیرناشتا (BS)")
                                        .answerType(AnswerType.NUMBER)
//                                        .canWriteDesc(true)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .question("فشار خون (BP)")
                                        .answerType(AnswerType.NUMBER)
//                                        .canWriteDesc(true)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .question("فشار خون اول (First BP)")
                                        .answerType(AnswerType.NUMBER)
//                                        .canWriteDesc(true)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .question("دارو (Medicine)")
                                        .answerType(AnswerType.TEXT)
//                                        .canWriteDesc(true)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .question("فشار خون دوم (Second BP)")
                                        .answerType(AnswerType.NUMBER)
//                                        .canWriteDesc(true)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .question("دارو بار دوم (Medicine 2)")
                                        .answerType(AnswerType.TEXT)
//                                        .canWriteDesc(true)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .question("فشار خون سوم (Third BP)")
                                        .answerType(AnswerType.NUMBER)
//                                        .canWriteDesc(true)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .question("توضیحات")
                                        .answerType(AnswerType.LONG_TEXT)
                                        .build()
                        )
                )
                .build();
    }
}
