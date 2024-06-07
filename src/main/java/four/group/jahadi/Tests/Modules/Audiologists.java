package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.Question.TableQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static four.group.jahadi.Tests.Modules.ModuleSeeder.commonSubModules;

public class Audiologists {

    public static Module seed() {

        SubModule room1 = SubModule
                .builder()
                .id(new ObjectId())
                .name("اتاق شنوایی 1")
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .questionType(QuestionType.SIMPLE)
                                        .question("نتیجه اتوسکوپی")
                                        .answerType(AnswerType.TEXT)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .questionType(QuestionType.SIMPLE)
                                        .question("ویزیت")
                                        .answerType(AnswerType.LONG_TEXT)
                                        .build(),
                                TableQuestion
                                        .builder()
                                        .required(true)
                                        .questionType(QuestionType.TABLE)
                                        .headers(List.of("R", "L"))
                                        .rowsCount(1)
                                        .answerType(AnswerType.NUMBER)
                                        .build(),
                                TableQuestion
                                        .builder()
                                        .required(true)
                                        .questionType(QuestionType.TABLE)
                                        .headers(List.of("Frequency", "500", "1000", "2000", "3000", "4000", "6000", "8000"))
                                        .firstColumn(List.of("TH (right)", "TH (left)"))
                                        .rowsCount(2)
                                        .answerType(AnswerType.NUMBER)
                                        .build()
                        )
                )
                .build();

        SubModule room2 = SubModule
                .builder()
                .id(new ObjectId())
                .name("اتاق شنوایی 2")
                .questions(
                        List.of(
                                TableQuestion
                                        .builder()
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
                                        .required(true)
                                        .questionType(QuestionType.TABLE)
                                        .title("Weber")
                                        .headers(List.of("Freq", ""))
                                        .firstColumn(List.of("250", "500", "1000"))
                                        .rowsCount(3)
                                        .answerType(AnswerType.NUMBER)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .questionType(QuestionType.SIMPLE)
                                        .answerType(AnswerType.TICK)
                                        .options(
                                                Arrays.stream(four.group.jahadi.Enums.Module.Audiologists.values()).map(audiologists -> new PairValue(
                                                        audiologists.name(),
                                                        audiologists.getFaTranslate()
                                                )).collect(Collectors.toList())
                                        )
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .questionType(QuestionType.SIMPLE)
                                        .required(true)
                                        .question("تشحیص")
                                        .answerType(AnswerType.LONG_TEXT)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .questionType(QuestionType.SIMPLE)
                                        .required(false)
                                        .question("آپلود فایل")
                                        .answerType(AnswerType.UPLOAD)
                                        .build()
                        )
                )
                .build();

        SubModule externalRefer = SubModule
                .builder()
                .name("ارجاع به متخصصان گوش حلق بینی")
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .questionType(QuestionType.SIMPLE)
                                        .required(true)
                                        .question("علت ارجاع")
                                        .answerType(AnswerType.LONG_TEXT)
                                        .build()
                        )
                )
                .build();

        return Module.builder()
                .name("شنوایی سنج")
                .subModules(
                        List.of(room1, room2, commonSubModules.get("externalReferral"), externalRefer)
                )
                .build();
    }
}
