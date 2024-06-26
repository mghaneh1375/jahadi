package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MiniParaClinic {
    public static SubModule make() {
        return SubModule
                .builder()
                .id(new ObjectId())
                .name("خدمات پاراکلینیک")
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
                                        .id(new ObjectId())
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
                                                                        .id(new ObjectId())
                                                                        .questionType(QuestionType.SIMPLE)
                                                                        .question(itr.getFaTranslate())
                                                                        .canWriteDesc(true)
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
