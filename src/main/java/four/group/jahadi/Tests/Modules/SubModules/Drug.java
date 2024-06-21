package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.ListQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Drug {

    public static SubModule make() {
        return SubModule
                .builder()
                .questions(
                        List.of(
                                ListQuestion
                                        .builder()
                                        .questionType(QuestionType.LIST)
                                        .questions(
                                                List.of(
                                                        SimpleQuestion
                                                                .builder()
                                                                .questionType(QuestionType.SIMPLE)
                                                                .required(true)
                                                                .question("نام دارو")
                                                                .answerType(AnswerType.SELECT)
                                                                .dynamicOptions("drugs")
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .questionType(QuestionType.SIMPLE)
                                                                .question("تعداد")
                                                                .required(true)
                                                                .answerType(AnswerType.NUMBER)
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .questionType(QuestionType.SIMPLE)
                                                                .question("مقدار مصرف")
                                                                .required(true)
                                                                .answerType(AnswerType.SELECT)
                                                                .options(
                                                                        Arrays.stream(
                                                                                four.group.jahadi.Enums.Drug.AmountOfUse.values()).map(
                                                                                item -> new PairValue(
                                                                                        item.name(),
                                                                                        item.getFaTranslate()
                                                                                )).collect(Collectors.toList())
                                                                ).build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .questionType(QuestionType.SIMPLE)
                                                                .question("طریقه مصرف")
                                                                .required(true)
                                                                .answerType(AnswerType.SELECT)
                                                                .options(
                                                                        Arrays.stream(
                                                                                four.group.jahadi.Enums.Drug.HowToUse.values()).map(
                                                                                item -> new PairValue(
                                                                                        item.name(),
                                                                                        item.getFaTranslate()
                                                                                )).collect(Collectors.toList())
                                                                ).build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .questionType(QuestionType.SIMPLE)
                                                                .question("زمان مصرف")
                                                                .required(true)
                                                                .answerType(AnswerType.SELECT)
                                                                .options(
                                                                        Arrays.stream(
                                                                                four.group.jahadi.Enums.Drug.UseTime.values()).map(
                                                                                item -> new PairValue(
                                                                                        item.name(),
                                                                                        item.getFaTranslate()
                                                                                )).collect(Collectors.toList())
                                                                ).build()
                                                )
                                        )
                                        .build()
                        )
                )
                .build();
    }
}
