package four.group.jahadi.Tests.Modules.SubModules.Sight;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
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

public class SightRoom {

    public static SubModule make() {
        return SubModule
                .builder()
                .id(new ObjectId())
                .name("اتاق بینایی")
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .question("ADD")
                                        .questionType(QuestionType.SIMPLE)
                                        .answerType(AnswerType.NUMBER)
                                        .build(),
                                CheckListGroupQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.CHECK_LIST)
                                        .sectionTitle("عینک تحویلی")
                                        .options(
                                                Arrays.stream(four.group.jahadi.Enums.Module.ShouldGive.values())
                                                        .map(itr -> new PairValue(
                                                                itr.name(),
                                                                itr.getFaTranslate()
                                                        ))
                                                        .collect(Collectors.toList())
                                        )
                                        .questions(
                                                List.of(
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(new ObjectId())
                                                                .question("عینک طبی")
                                                                .questionType(QuestionType.SIMPLE)
                                                                .answerType(AnswerType.TICK)
                                                                .required(true)
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(new ObjectId())
                                                                .question("عینک آفتابی")
                                                                .questionType(QuestionType.SIMPLE)
                                                                .answerType(AnswerType.TICK)
                                                                .required(true)
                                                                .build()
                                                )
                                        )
                                        .build(),
                                GroupQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.GROUP)
                                        .sectionTitle("عینک ساختنی")
                                        .questions(
                                                List.of(
                                                        TableQuestion
                                                                .builder()
                                                                .id(new ObjectId())
                                                                .required(true)
                                                                .questionType(QuestionType.TABLE)
                                                                .headers(List.of("...", "+/-", "SPH", "CYL", "VA"))
                                                                .firstColumn(List.of("OD", "OS"))
                                                                .rowsCount(2)
                                                                .answerType(AnswerType.DOUBLE)
                                                                .build(),
                                                        TableQuestion
                                                                .builder()
                                                                .id(new ObjectId())
                                                                .required(true)
                                                                .questionType(QuestionType.TABLE)
                                                                .headers(List.of("PD", "NPD"))
                                                                .rowsCount(1)
                                                                .cellLabel("(mm)")
                                                                .answerType(AnswerType.DOUBLE)
                                                                .build()
                                                )
                                        )
                                        .build()
                        )
                )
                .build();
    }

}
