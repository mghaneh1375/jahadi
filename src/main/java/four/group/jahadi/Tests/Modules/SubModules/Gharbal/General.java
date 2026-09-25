package four.group.jahadi.Tests.Modules.SubModules.Gharbal;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.DiseaseBackground;
import four.group.jahadi.Enums.Module.DrugBackground;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Tests.Modules.SubModules.Helper;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.List;

public class General {
    public static SubModule make(String moduleName, ObjectId referToOid) {
        String subModuleName = "غربالگری کلی";
        SubModule oldSubModule = Helper.findSubModule(moduleName, subModuleName);
        Question mainQuestion1 = Helper.findQuestionInSubModule(oldSubModule, "سابقه بیماری");
        Question mainQuestion2 = Helper.findQuestionInSubModule(oldSubModule, "سابقه مصرف دارو");
        Question mainQuestion3 = Helper.findQuestionInSubModule(oldSubModule, "قند خون ناشتا (FBS)");
        Question mainQuestion4 = Helper.findQuestionInSubModule(oldSubModule, "قند خون غیرناشتا (BS)");
        Question mainQuestion5 = Helper.findQuestionInSubModule(oldSubModule, "فشار خون اول (First BP)");
        Question mainQuestion6 = Helper.findQuestionInSubModule(oldSubModule, "دارو (Medicine)");
        Question mainQuestion7 = Helper.findQuestionInSubModule(oldSubModule, "فشار خون دوم (Second BP)");
        Question mainQuestion8 = Helper.findQuestionInSubModule(oldSubModule, "دارو بار دوم (Medicine 2)");
        Question mainQuestion9 = Helper.findQuestionInSubModule(oldSubModule, "فشار خون سوم (Third BP)");
        Question mainQuestion10 = Helper.findQuestionInSubModule(oldSubModule, "توضیحات");

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
                                        .sectionTitle("سابقه بیماری")
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
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "دیابت ملیتوس (DM)"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("دیابت ملیتوس (DM)")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "پرفشاری خون (HTN)"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("پرفشاری خون (HTN)")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "مشکل تیروئید"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("مشکل تیروئید")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build()
                                        ))
                                        .build(),
                                CheckListGroupQuestion
                                        .builder()
                                        .id(mainQuestion2 == null ? new ObjectId() : mainQuestion2.getId())
                                        .questionType(QuestionType.CHECK_LIST)
                                        .sectionTitle("سابقه مصرف دارو")
                                        .options(List.of(
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
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion2, "دیابت ملیتوس (DM)"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("دیابت ملیتوس (DM)")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion2, "پرفشاری خون (HTN)"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("پرفشاری خون (HTN)")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion2, "مشکل تیروئید"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("مشکل تیروئید")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build()
                                        ))
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion3 == null ? new ObjectId() : mainQuestion3.getId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .question("قند خون ناشتا (FBS)")
                                        .answerType(AnswerType.NUMBER)
//                                        .canWriteDesc(true)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion4 == null ? new ObjectId() : mainQuestion4.getId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .question("قند خون غیرناشتا (BS)")
                                        .answerType(AnswerType.NUMBER)
//                                        .canWriteDesc(true)
                                        .build(),
//                                SimpleQuestion
//                                        .builder()
//                                        .id(new ObjectId())
//                                        .questionType(QuestionType.SIMPLE)
//                                        .required(false)
//                                        .question("فشار خون (BP)")
//                                        .answerType(AnswerType.NUMBER)
////                                        .canWriteDesc(true)
//                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion5 == null ? new ObjectId() : mainQuestion5.getId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .question("فشار خون اول (First BP)")
                                        .answerType(AnswerType.NUMBER)
//                                        .canWriteDesc(true)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion6 == null ? new ObjectId() : mainQuestion6.getId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .question("دارو (Medicine)")
                                        .answerType(AnswerType.TEXT)
//                                        .canWriteDesc(true)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion7 == null ? new ObjectId() : mainQuestion7.getId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .question("فشار خون دوم (Second BP)")
                                        .answerType(AnswerType.NUMBER)
//                                        .canWriteDesc(true)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion8 == null ? new ObjectId() : mainQuestion8.getId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .question("دارو بار دوم (Medicine 2)")
                                        .answerType(AnswerType.TEXT)
//                                        .canWriteDesc(true)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion9 == null ? new ObjectId() : mainQuestion9.getId())
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .question("فشار خون سوم (Third BP)")
                                        .answerType(AnswerType.NUMBER)
//                                        .canWriteDesc(true)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion10 == null ? new ObjectId() : mainQuestion10.getId())
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
