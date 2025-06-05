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

import static four.group.jahadi.Tests.Modules.ModuleSeeder.moduleIds;

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
                                        .required(false)
                                        .answerType(AnswerType.TEXT)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.SIMPLE)
                                        .question("ویزیت")
                                        .required(false)
                                        .answerType(AnswerType.LONG_TEXT)
                                        .build(),
                                TableQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .required(false)
                                        .title("اودیومتری")
                                        .questionType(QuestionType.TABLE)
                                        .headers(List.of("Frequency", "250", "500", "750", "1000", "1500", "2000", "3000", "4000", "6000", "8000"))
                                        .firstColumn(List.of("TH (right)", "TH (left)"))
                                        .rowsCount(2)
                                        .answerType(AnswerType.NUMBER)
                                        .build(),
                                GroupQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.GROUP)
                                        .sectionTitle("تیمپانومتری")
                                        .questions(
                                                List.of(
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(new ObjectId())
                                                                .required(false)
                                                                .question("R")
                                                                .answerType(AnswerType.SELECT)
                                                                .questionType(QuestionType.SIMPLE)
                                                                .options(
                                                                        Arrays.stream(
                                                                                four.group.jahadi.Enums.Timpanometry.values()).map(
                                                                                item -> new PairValue(
                                                                                        item.name(),
                                                                                        item.getFaTranslate()
                                                                                )).collect(Collectors.toList())
                                                                )
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(new ObjectId())
                                                                .required(false)
                                                                .question("L")
                                                                .answerType(AnswerType.SELECT)
                                                                .questionType(QuestionType.SIMPLE)
                                                                .options(
                                                                        Arrays.stream(
                                                                                four.group.jahadi.Enums.Timpanometry.values()).map(
                                                                                item -> new PairValue(
                                                                                        item.name(),
                                                                                        item.getFaTranslate()
                                                                                )).collect(Collectors.toList())
                                                                )
                                                                .build()
                                                )
                                        )
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
                                        .required(false)
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
                                        .required(false)
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
                .isReferral(true)
                .referTo(moduleIds.get("متخصص گوش و حلق و بینی"))
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
                                                        .required(false)
                                                        .build()).collect(Collectors.toList())
                                        )
                                        .build(),
                                GroupQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.GROUP)
                                        .sectionTitle("تشخیص شنوایی شناس")
                                        .questions(
                                                List.of(
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(new ObjectId())
                                                                .questionType(QuestionType.SIMPLE)
                                                                .required(false)
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
//                                        .required(false)
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
