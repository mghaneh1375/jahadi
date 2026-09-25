package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static four.group.jahadi.Tests.Modules.ModuleSeeder.moduleIds;

public class MiniParaClinic {
    public static SubModule make(String moduleName) {
        String subModuleName = "خدمات پاراکلینیک";
        SubModule oldSubModule = Helper.findSubModule(moduleName, subModuleName);
        Question mainQuestion1 = Helper.findQuestionInSubModule(oldSubModule, "خدمات پاراکلینیک");

        return SubModule
                .builder()
                .id(oldSubModule == null ? new ObjectId() : oldSubModule.getId())
                .name(subModuleName)
                .isReferral(true)
                .referTo(moduleIds.get("پاراکلینیک"))
                .questions(
                        List.of(
//                                SimpleQuestion
//                                        .builder()
//                                        .id(new ObjectId())
//                                        .required(true)
//                                        .questionType(QuestionType.SIMPLE)
//                                        .answerType(AnswerType.TEXT)
//                                        .question("توضیحات (مربوط به عملیات احیا)")
//                                        .build(),
//                                SimpleQuestion
//                                        .builder()
//                                        .id(new ObjectId())
//                                        .required(true)
//                                        .questionType(QuestionType.SIMPLE)
//                                        .answerType(AnswerType.TEXT)
//                                        .question("افرادی که حضور داشتند")
//                                        .build(),
                                CheckListGroupQuestion
                                        .builder()
                                        .id(mainQuestion1 == null ? new ObjectId() : mainQuestion1.getId())
                                        .questionType(QuestionType.CHECK_LIST)
                                        .sectionTitle("خدمات پاراکلینیک")
                                        .options(
                                                List.of(
                                                        new PairValue("SUGGEST", "تجویز خدمت")
                                                )
                                        )
                                        .questions(
                                                Arrays.stream(four.group.jahadi.Enums.Module.LimitedParaClinic.values())
                                                        .map(itr ->
                                                                SimpleQuestion
                                                                        .builder()
                                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, itr.getFaTranslate()))
                                                                        .questionType(QuestionType.SIMPLE)
                                                                        .question(itr.getFaTranslate())
                                                                        .canWriteDesc(true)
                                                                        .required(false)
                                                                        .answerType(AnswerType.TICK)
                                                                        .build()
                                                        ).collect(Collectors.toList())
                                        )
                                        .canWriteDesc(true)
                                        .build()
                        )
                )
                .build();
    }

}
