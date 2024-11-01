package four.group.jahadi.Tests.Modules.SubModules.Sampler;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.DoneOrNot;
import four.group.jahadi.Enums.Module.IsOrNot;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.GroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.List;

public class Step1 {

    public static SubModule make() {
        return SubModule
                .builder()
                .id(new ObjectId())
                .name("جدول آزمایش")
                .isReferral(false)
                .questions(List.of(
                        CheckListGroupQuestion
                                .builder()
                                .id(new ObjectId())
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
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("لوله تخت")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("لوله EDTA")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("نمونه ادرار")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .question("ناشتا")
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .build()
                                ))
                                .build(),
                        GroupQuestion
                                .builder()
                                .id(new ObjectId())
                                .questionType(QuestionType.GROUP)
                                .sectionTitle("جزئیات آزمایش")
                                .questions(List.of(
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
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
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.NUMBER)
                                                .required(false)
                                                .question("مدت زمان بارداری (به روز)")
                                                .build()
                                ))
                                .build(),
                        SimpleQuestion
                                .builder()
                                .id(new ObjectId())
                                .questionType(QuestionType.SIMPLE)
                                .answerType(AnswerType.DATE)
                                .required(false)
                                .question("LMP")
                                .build()
                ))
                .build();
    }

}
