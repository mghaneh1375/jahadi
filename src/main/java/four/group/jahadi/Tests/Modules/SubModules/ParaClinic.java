package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static four.group.jahadi.Tests.Modules.ModuleSeeder.moduleIds;

public class ParaClinic {

    public static Module seed() {

//        SubModule history = SubModule
//                .builder()
//                .id(new ObjectId())
//                .name("مشاهده تجویز قبلی")
//                .readOnlyModuleId(moduleIds.get("پزشک عمومی"))
//                .readOnlySubModuleId(miniParaClinicSubModuleId)
//                .build();

        SubModule services = SubModule
                .builder()
                .id(new ObjectId())
                .name("خدمات پاراکلینیک")
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .required(false)
                                        .questionType(QuestionType.SIMPLE)
                                        .answerType(AnswerType.TEXT)
                                        .question("توضیحات (مربوط به عملیات احیا)")
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .required(false)
                                        .questionType(QuestionType.SIMPLE)
                                        .answerType(AnswerType.TEXT)
                                        .question("افرادی که حضور داشتند")
                                        .build(),
                                CheckListGroupQuestion
                                        .builder()
                                        .id(new ObjectId())
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
                                                                        .id(new ObjectId())
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
                                        .id(new ObjectId())
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
                .name("پاراکلینیک")
                .icon("")
//                .subModules(List.of(history, services))
                .subModules(List.of(services))
                .build();
    }

}
