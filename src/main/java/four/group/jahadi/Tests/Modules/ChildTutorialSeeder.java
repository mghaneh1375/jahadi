package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.DeliveryStatus;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Enums.Module.Shepesh;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;

import java.util.List;

public class ChildTutorialSeeder {

    public static Module seed() {

        return Module
                .builder()
                .name("آموزش کودکان")
                .icon("")
                .subModules(
                        List.of(
                                SubModule
                                        .builder()
                                        .name("آموزش پس از پزشک")
                                        .questions(
                                                List.of(
                                                        SimpleQuestion
                                                                .builder()
                                                                .questionType(QuestionType.SIMPLE)
                                                                .required(false)
                                                                .question("قد")
                                                                .answerType(AnswerType.NUMBER)
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .questionType(QuestionType.SIMPLE)
                                                                .required(false)
                                                                .question("وزن")
                                                                .answerType(AnswerType.NUMBER)
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .questionType(QuestionType.SIMPLE)
                                                                .required(false)
                                                                .question("BMI")
                                                                .answerType(AnswerType.NUMBER)
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .questionType(QuestionType.SIMPLE)
                                                                .required(false)
                                                                .question("شپش")
                                                                .answerType(AnswerType.SELECT)
                                                                .options(
                                                                        List.of(
                                                                                new PairValue(Shepesh.NORMAL.name(), Shepesh.NORMAL.getFaTranslate()),
                                                                                new PairValue(Shepesh.NIT.name(), Shepesh.NIT.getFaTranslate()),
                                                                                new PairValue(Shepesh.BALEGH.name(), Shepesh.BALEGH.getFaTranslate())
                                                                        )
                                                                )
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .questionType(QuestionType.SIMPLE)
                                                                .required(false)
                                                                .question("بسته فرهنگی")
                                                                .answerType(AnswerType.TICK)
                                                                .options(
                                                                        List.of(
                                                                                new PairValue(
                                                                                        DeliveryStatus.DELIVERED.name(),
                                                                                        DeliveryStatus.DELIVERED.getFaTranslate()
                                                                                ),
                                                                                new PairValue(
                                                                                        DeliveryStatus.NOT_DELIVERED.name(),
                                                                                        DeliveryStatus.NOT_DELIVERED.getFaTranslate()
                                                                                )
                                                                        )
                                                                )
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .questionType(QuestionType.SIMPLE)
                                                                .required(false)
                                                                .question("شامپو")
                                                                .answerType(AnswerType.TICK)
                                                                .options(
                                                                        List.of(
                                                                                new PairValue(
                                                                                        DeliveryStatus.DELIVERED.name(),
                                                                                        DeliveryStatus.DELIVERED.getFaTranslate()
                                                                                ),
                                                                                new PairValue(
                                                                                        DeliveryStatus.NOT_DELIVERED.name(),
                                                                                        DeliveryStatus.NOT_DELIVERED.getFaTranslate()
                                                                                )
                                                                        )
                                                                )
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .questionType(QuestionType.SIMPLE)
                                                                .required(false)
                                                                .question("توضیحات")
                                                                .answerType(AnswerType.LONG_TEXT)
                                                                .build()
                                                )
                                        )
                                        .postAction("submit_in_post_doctor_table")
                                        .build()
                        )
                )
                .build();
    }

}
