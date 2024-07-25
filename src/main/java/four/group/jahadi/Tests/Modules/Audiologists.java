package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.HaveOrNot;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.GroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.Question.TableQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Audiologists {

    public static Module seed() {

        SubModule room1 = SubModule
                .builder()
                .id(new ObjectId())
                .name("اتاق شنوایی 1")
                .isReferral(false)
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.SIMPLE)
                                        .question("نتیجه اتوسکوپی")
                                        .required(true)
                                        .answerType(AnswerType.TEXT)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.SIMPLE)
                                        .question("ویزیت")
                                        .required(true)
                                        .answerType(AnswerType.LONG_TEXT)
                                        .build(),
                                TableQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .required(true)
                                        .title("اطلاعات")
                                        .questionType(QuestionType.TABLE)
                                        .headers(List.of("Frequency", "500", "1000", "2000", "3000", "4000", "6000", "8000"))
                                        .firstColumn(List.of("TH (right)", "TH (left)"))
                                        .rowsCount(2)
                                        .answerType(AnswerType.NUMBER)
                                        .build(),
                                TableQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .required(true)
                                        .questionType(QuestionType.TABLE)
                                        .headers(List.of("R", "L"))
                                        .title("اطلاعات")
                                        .rowsCount(1)
                                        .answerType(AnswerType.NUMBER)
                                        .build()
                        )
                )
                .build();

        SubModule room2 = SubModule
                .builder()
                .id(new ObjectId())
                .name("اتاق شنوایی 2")
                .isReferral(false)
                .questions(
                        List.of(
                                TableQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .required(true)
                                        .questionType(QuestionType.TABLE)
                                        .title("Rinne")
                                        .headers(List.of("Freq", "L", "R"))
                                        .firstColumn(List.of("250", "500", "1000"))
                                        .rowsCount(3)
                                        .answerType(AnswerType.NUMBER)
                                        .build(),
                                TableQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .required(true)
                                        .questionType(QuestionType.TABLE)
                                        .title("Weber")
                                        .headers(List.of("Freq", ""))
                                        .firstColumn(List.of("250", "500", "1000"))
                                        .rowsCount(3)
                                        .answerType(AnswerType.NUMBER)
                                        .build()
                        )
                )
                .build();

        SubModule room3 = SubModule
                .builder()
                .id(new ObjectId())
                .name("اتاق شنوایی 3")
                .isReferral(false)
                .questions(
                        List.of(
                                CheckListGroupQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .sectionTitle("سوالات")
                                        .questionType(QuestionType.CHECK_LIST)
                                        .options(
                                                Arrays.stream(HaveOrNot.values()).map(itr -> new PairValue(
                                                        itr.name(),
                                                        itr.getFaTranslate()
                                                )).collect(Collectors.toList())
                                        )
                                        .questions(
                                                Arrays.stream(four.group.jahadi.Enums.Module.Audiologists.values()).map(audiologists -> SimpleQuestion
                                                        .builder()
                                                        .id(new ObjectId())
                                                        .question(audiologists.getFaTranslate())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .answerType(AnswerType.TICK)
                                                        .required(true)
                                                        .build()).collect(Collectors.toList())
                                        )
                                        .build(),
                                GroupQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.GROUP)
                                        .sectionTitle("تشخیص شنوایی سنج")
                                        .questions(
                                                List.of(
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(new ObjectId())
                                                                .questionType(QuestionType.SIMPLE)
                                                                .required(true)
                                                                .question("تشحیص")
                                                                .answerType(AnswerType.LONG_TEXT)
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(new ObjectId())
                                                                .questionType(QuestionType.SIMPLE)
                                                                .required(false)
                                                                .question("آپلود فایل")
                                                                .answerType(AnswerType.UPLOAD)
                                                                .build()
                                                )
                                        )
                                        .build()

                        )).build();
//        SubModule externalRefer = SubModule
//                .builder()
//                .name("ارجاع به متخصصان گوش حلق بینی")
//                .questions(
//                        List.of(
//                                SimpleQuestion
//                                        .builder()
//                                        .questionType(QuestionType.SIMPLE)
//                                        .required(true)
//                                        .question("علت ارجاع")
//                                        .answerType(AnswerType.LONG_TEXT)
//                                        .build()
//                        )
//                )
//                .build();

        return Module.builder()
                .name("شنوایی")
                .icon("")
                .tabName("توان بخشی")
                .subModules(
                        List.of(room1, room2, room3)
                )
                .build();
    }
}
