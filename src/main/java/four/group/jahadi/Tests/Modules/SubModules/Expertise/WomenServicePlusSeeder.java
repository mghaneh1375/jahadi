package four.group.jahadi.Tests.Modules.SubModules.Expertise;

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

public class WomenServicePlusSeeder {
    public static SubModule make() {
        return SubModule
                .builder()
                .id(new ObjectId())
                .name("خدمات پلاس")
                .questions(
                        List.of(
                                CheckListGroupQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.CHECK_LIST)
                                        .sectionTitle("خدمات پلاس")
                                        .options(
                                                Arrays.stream(four.group.jahadi.Enums.Module.DoneOrNot.values())
                                                        .map(itr -> new PairValue(
                                                                itr.name(),
                                                                itr.getFaTranslate()
                                                        ))
                                                        .collect(Collectors.toList())
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
                                        .canWriteReport(true)
                                        .canWriteReason(true)
                                        .canWriteSampleInfoDesc(true)
                                        .canUploadFile(true)
                                        .build()
                        )
                )
                .build();
    }
}
