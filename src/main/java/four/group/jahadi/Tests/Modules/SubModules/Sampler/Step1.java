package four.group.jahadi.Tests.Modules.SubModules.Sampler;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.DoneOrNot;
import four.group.jahadi.Enums.Module.IsOrNot;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.GroupQuestion;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Tests.Modules.SubModules.Helper;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.List;

public class Step1 {

    public static SubModule make(String moduleName) {
        String subModuleName = "جدول آزمایش";
        SubModule oldSubModule = Helper.findSubModule(moduleName, subModuleName);
        Question mainQuestion1 = Helper.findQuestionInSubModule(oldSubModule, "خدمات آزمایش");
        Question mainQuestion2 = Helper.findQuestionInSubModule(oldSubModule, "جزئیات آزمایش");
        Question mainQuestion3 = Helper.findQuestionInSubModule(oldSubModule, "LMP");

        return SubModule
                .builder()
                .id(oldSubModule == null ? new ObjectId() : oldSubModule.getId())
                .name(subModuleName)
                .isReferral(false)
                .questions(List.of(
                        CheckListGroupQuestion
                                .builder()
                                .id(mainQuestion1 == null ? new ObjectId() : mainQuestion1.getId())
                                .questionType(QuestionType.CHECK_LIST)
                                .sectionTitle("خدمات آزمایش")
                                .options(List.of(
                                        new PairValue(
                                                DoneOrNot.DONE.name(),
                                                DoneOrNot.DONE.getFaTranslate()
                                        ),
                                        new PairValue(
                                                DoneOrNot.NOT_DONE.name(),
                                                DoneOrNot.NOT_DONE.getFaTranslate()
                                        )
                                ))
                                .questions(List.of(
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "لوله لخته"))
                                                .questionType(QuestionType.SIMPLE)
                                                .question("لوله لخته")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "لوله EDTA"))
                                                .questionType(QuestionType.SIMPLE)
                                                .question("لوله EDTA")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "نمونه ادرار"))
                                                .questionType(QuestionType.SIMPLE)
                                                .question("نمونه ادرار")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "ناشتا"))
                                                .questionType(QuestionType.SIMPLE)
                                                .question("ناشتا")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build()
                                ))
                                .build(),
                        GroupQuestion
                                .builder()
                                .id(mainQuestion2 == null ? new ObjectId() : mainQuestion2.getId())
                                .questionType(QuestionType.GROUP)
                                .sectionTitle("جزئیات آزمایش")
                                .questions(List.of(
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList((GroupQuestion) mainQuestion2, "بیمار باردار"))
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.RADIO)
                                                .required(false)
                                                .options(List.of(
                                                        new PairValue(
                                                                IsOrNot.IS.name(),
                                                                IsOrNot.IS.getFaTranslate()
                                                        ),
                                                        new PairValue(
                                                                IsOrNot.NOT.name(),
                                                                IsOrNot.NOT.getFaTranslate()
                                                        )
                                                ))
                                                .question("بیمار باردار")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList((GroupQuestion) mainQuestion2, "مدت زمان بارداری (به روز)"))
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.NUMBER)
                                                .required(false)
                                                .question("مدت زمان بارداری (به روز)")
                                                .build()
                                ))
                                .build(),
                        SimpleQuestion
                                .builder()
                                .id(mainQuestion3 == null ? new ObjectId() : mainQuestion3.getId())
                                .questionType(QuestionType.SIMPLE)
                                .answerType(AnswerType.DATE)
                                .required(false)
                                .question("LMP")
                                .build()
                ))
                .build();
    }

}
