package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.HaveOrNot;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.*;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Tests.Modules.SubModules.Helper;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static four.group.jahadi.Tests.Modules.ModuleSeeder.moduleIds;

public class Audiologists {

    public static Module seed() {
        String moduleName = "شنوایی";

        String subModuleName1 = "اتاق شنوایی 1";
        SubModule oldSubModule1 = Helper.findSubModule(moduleName, subModuleName1);

        Question mainQuestion11 = Helper.findQuestionInSubModule(oldSubModule1, "نتیجه اتوسکوپی");
        Question mainQuestion12 = Helper.findQuestionInSubModule(oldSubModule1, "ویزیت");
        Question mainQuestion13 = Helper.findQuestionInSubModule(oldSubModule1, List.of("Frequency", "250", "500", "750", "1000", "1500", "2000", "3000", "4000", "6000", "8000"));
        Question mainQuestion14 = Helper.findQuestionInSubModule(oldSubModule1, "تیمپانومتری");

        String subModuleName2 = "اتاق شنوایی 2";
        SubModule oldSubModule2 = Helper.findSubModule(moduleName, subModuleName2);

        ObjectId mainQuestion21 = oldSubModule2 == null || oldSubModule2.getQuestions().size() < 1 ? new ObjectId() : oldSubModule2.getQuestions().get(0).getId();
        ObjectId mainQuestion22 = oldSubModule2 == null || oldSubModule2.getQuestions().size() < 2 ? new ObjectId() : oldSubModule2.getQuestions().get(1).getId();

        String subModuleName3 = "اتاق شنوایی 3";
        SubModule oldSubModule3 = Helper.findSubModule(moduleName, subModuleName3);

        Question mainQuestion31 = Helper.findQuestionInSubModule(oldSubModule3, "سوالات");
        Question mainQuestion32 = Helper.findQuestionInSubModule(oldSubModule3, "تشخیص شنوایی شناس");

        SubModule room1 = SubModule
                .builder()
                .id(oldSubModule1 == null ? new ObjectId() : oldSubModule1.getId())
                .name(subModuleName1)
                .isReferral(false)
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion11 == null ? new ObjectId() : mainQuestion11.getId())
                                        .questionType(QuestionType.SIMPLE)
                                        .question("نتیجه اتوسکوپی")
                                        .required(false)
                                        .answerType(AnswerType.TEXT)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion12 == null ? new ObjectId() : mainQuestion12.getId())
                                        .questionType(QuestionType.SIMPLE)
                                        .question("ویزیت")
                                        .required(false)
                                        .answerType(AnswerType.LONG_TEXT)
                                        .build(),
                                TableQuestion
                                        .builder()
                                        .id(mainQuestion13 == null ? new ObjectId() : mainQuestion13.getId())
                                        .required(false)
                                        .title("اودیومتری")
                                        .questionType(QuestionType.TABLE)
                                        .headers(List.of("Frequency", "250", "500", "750", "1000", "1500", "2000", "3000", "4000", "6000", "8000"))
                                        .firstColumn(List.of("TH (right)", "TH (left)"))
                                        .rowsCount(2)
                                        .answerType(AnswerType.TEXT)
                                        .build(),
                                GroupQuestion
                                        .builder()
                                        .id(mainQuestion14 == null ? new ObjectId() : mainQuestion14.getId())
                                        .questionType(QuestionType.GROUP)
                                        .sectionTitle("تیمپانومتری")
                                        .questions(
                                                List.of(
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(Helper.findSimpleQuestionInList((GroupQuestion) mainQuestion14, "R"))
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
                                                                .id(Helper.findSimpleQuestionInList((GroupQuestion) mainQuestion14, "L"))
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
                .id(oldSubModule2 == null ? new ObjectId() : oldSubModule2.getId())
                .name(subModuleName2)
                .isReferral(false)
                .questions(
                        List.of(
                                TableQuestion
                                        .builder()
                                        .id(mainQuestion21)
                                        .required(false)
                                        .questionType(QuestionType.TABLE)
                                        .title("Rinne")
                                        .headers(List.of("Freq", "L", "R"))
                                        .firstColumn(List.of("250", "500", "1000"))
                                        .rowsCount(3)
                                        .answerType(AnswerType.TEXT)
                                        .build(),
                                TableQuestion
                                        .builder()
                                        .id(mainQuestion22)
                                        .required(false)
                                        .questionType(QuestionType.TABLE)
                                        .title("Weber")
                                        .headers(List.of("Freq", "L", "R"))
                                        .firstColumn(List.of("250", "500", "1000"))
                                        .rowsCount(3)
                                        .answerType(AnswerType.TEXT)
                                        .build()
                        )
                )
                .build();

        SubModule room3 = SubModule
                .builder()
                .id(oldSubModule3 == null ? new ObjectId() : oldSubModule3.getId())
                .name(subModuleName3)
                .isReferral(true)
                .referTo(moduleIds.get("متخصص گوش و حلق و بینی"))
                .questions(
                        List.of(
                                CheckListGroupQuestion
                                        .builder()
                                        .id(mainQuestion31 == null ? new ObjectId() : mainQuestion31.getId())
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
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion31, audiologists.getFaTranslate()))
                                                        .question(audiologists.getFaTranslate())
                                                        .questionType(QuestionType.SIMPLE)
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build()).collect(Collectors.toList())
                                        )
                                        .build(),
                                GroupQuestion
                                        .builder()
                                        .id(mainQuestion32 == null ? new ObjectId() : mainQuestion32.getId())
                                        .questionType(QuestionType.GROUP)
                                        .sectionTitle("تشخیص شنوایی شناس")
                                        .questions(
                                                List.of(
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(Helper.findSimpleQuestionInList((GroupQuestion) mainQuestion32, "تشحیص"))
                                                                .questionType(QuestionType.SIMPLE)
                                                                .required(false)
                                                                .question("تشحیص")
                                                                .answerType(AnswerType.LONG_TEXT)
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(Helper.findSimpleQuestionInList((GroupQuestion) mainQuestion32, "آپلود فایل"))
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
                .name(moduleName)
                .icon("")
                .tabName("توان بخشی")
                .subModules(
                        List.of(room1, room2, room3)
                )
                .build();
    }
}
