package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParaClinic {

    public static Module seed() {

        String moduleName = "پاراکلینیک";
        String subModuleName = "خدمات پاراکلینیک";
        SubModule oldSubModule = Helper.findSubModule(moduleName, subModuleName);

//        SubModule history = SubModule
//                .builder()
//                .id(new ObjectId())
//                .name("مشاهده تجویز قبلی")
//                .readOnlyModuleId(moduleIds.get("پزشک عمومی"))
//                .readOnlySubModuleId(miniParaClinicSubModuleId)
//                .build();

        Question mainQuestion1 = Helper.findQuestionInSubModule(oldSubModule, "توضیحات (مربوط به عملیات احیا)");
        Question mainQuestion2 = Helper.findQuestionInSubModule(oldSubModule, "افرادی که حضور داشتند");
        Question mainQuestion3 = Helper.findQuestionInSubModule(oldSubModule, "خدمات پاراکلینیک");
        Question mainQuestion4 = Helper.findQuestionInSubModule(oldSubModule, "توضیحات");

        SubModule services = SubModule
                .builder()
                .id(oldSubModule == null ? new ObjectId() : oldSubModule.getId())
                .name(subModuleName)
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion1 == null ? new ObjectId() : mainQuestion1.getId())
                                        .required(false)
                                        .questionType(QuestionType.SIMPLE)
                                        .answerType(AnswerType.TEXT)
                                        .question("توضیحات (مربوط به عملیات احیا)")
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion2 == null ? new ObjectId() : mainQuestion2.getId())
                                        .required(false)
                                        .questionType(QuestionType.SIMPLE)
                                        .answerType(AnswerType.TEXT)
                                        .question("افرادی که حضور داشتند")
                                        .build(),
                                CheckListGroupQuestion
                                        .builder()
                                        .id(mainQuestion3 == null ? new ObjectId() : mainQuestion3.getId())
                                        .questionType(QuestionType.CHECK_LIST)
                                        .sectionTitle("خدمات پاراکلینیک")
                                        .options(
                                                Arrays.stream(four.group.jahadi.Enums.Module.ParaClinicAnswers.values())
                                                        .map(itr -> new PairValue(
                                                                itr.name(),
                                                                itr.getFaTranslate()
                                                        ))
                                                        .collect(Collectors.toList())
                                        )
                                        .questions(
                                                Arrays.stream(four.group.jahadi.Enums.Module.AllParaClinic.values())
                                                        .map(itr ->
                                                                SimpleQuestion
                                                                        .builder()
                                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion3, itr.getFaTranslate()))
                                                                        .questionType(QuestionType.SIMPLE)
                                                                        .question(itr.getFaTranslate())
                                                                        .answerType(AnswerType.TICK)
                                                                        .required(false)
                                                                        .canWriteDesc(true)
                                                                        .build()
                                                        ).collect(Collectors.toList())
                                        )
                                        .canWriteDesc(true)
                                        .canUploadFile(true)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(mainQuestion4 == null ? new ObjectId() : mainQuestion4.getId())
                                        .questionType(QuestionType.SIMPLE)
                                        .answerType(AnswerType.LONG_TEXT)
                                        .question("توضیحات")
                                        .required(false)
                                        .build()
                        )
                )
                .build();

        return Module
                .builder()
                .tabName("پاراکلینیک")
                .name(moduleName)
                .icon("icon-injection-1")
//                .subModules(List.of(history, services))
                .subModules(List.of(services))
                .build();
    }

}
