package four.group.jahadi.Tests.Modules.SubModules.Physiotheraphy;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.MultiAnswerQuestion;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Tests.Modules.SubModules.Helper;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PhysiotherapyForm {
    public static SubModule make(String moduleName) {
        String subModuleName = "اقدامات";
        SubModule oldSubModule = Helper.findSubModule(moduleName, subModuleName);
        Question mainQuestion1 = Helper.findQuestionInSubModule(oldSubModule, "");
        ObjectId question1 = Helper.findSimpleQuestionInList((MultiAnswerQuestion) mainQuestion1, "نام اقدام");
        ObjectId question2 = Helper.findSimpleQuestionInList((MultiAnswerQuestion) mainQuestion1, "تعداد جلسات");
        ObjectId question3 = Helper.findSimpleQuestionInList((MultiAnswerQuestion) mainQuestion1, "وضعیت");
        ObjectId question4 = Helper.findSimpleQuestionInList((MultiAnswerQuestion) mainQuestion1, "توضیحات");

        return SubModule
                .builder()
                .id(oldSubModule == null ? new ObjectId() : oldSubModule.getId())
                .name(subModuleName)
                .isReferral(false)
                .questions(
                        List.of(
                                MultiAnswerQuestion
                                        .builder()
                                        .id(
                                                mainQuestion1 == null || mainQuestion1.getId() == null
                                                        ? new ObjectId()
                                                        : mainQuestion1.getId()
                                        )
                                        .sectionTitle("")
                                        .questionType(QuestionType.MULTI_ANSWER)
                                        .questions(
                                                List.of(
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(question1 == null ? new ObjectId() : question1)
                                                                .questionType(QuestionType.SIMPLE)
                                                                .question("نام اقدام")
                                                                .answerType(AnswerType.TEXT)
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(question2 == null ? new ObjectId() : question2)
                                                                .questionType(QuestionType.SIMPLE)
                                                                .question("تعداد جلسات")
                                                                .answerType(AnswerType.NUMBER)
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(question3 == null ? new ObjectId() : question3)
                                                                .questionType(QuestionType.SIMPLE)
                                                                .question("وضعیت")
                                                                .answerType(AnswerType.TICK)
                                                                .options(
                                                                        Arrays.stream(four.group.jahadi.Enums.Module.DoneOrNot.values())
                                                                                .map(itr -> new PairValue(
                                                                                        itr.name(),
                                                                                        itr.getFaTranslate()
                                                                                ))
                                                                                .collect(Collectors.toList())
                                                                )
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(question4 == null ? new ObjectId() : question4)
                                                                .questionType(QuestionType.SIMPLE)
                                                                .question("توضیحات")
                                                                .answerType(AnswerType.LONG_TEXT)
                                                                .build()
                                                )
                                        )
                                        .build()
                        )
                )
                .build();
    }
}
