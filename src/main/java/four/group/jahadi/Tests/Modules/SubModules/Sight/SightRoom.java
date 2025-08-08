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

import static four.group.jahadi.Tests.Modules.ModuleSeeder.moduleIds;

public class SightRoom {

    public static SubModule make() {
        return SubModule
                .builder()
                .id(new ObjectId())
                .name("اتاق بینایی")
                .isReferral(true)
                .referTo(moduleIds.get("متخصص چشم پزشکی"))
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .question("عینک مطالعه")
                                        .questionType(QuestionType.CHECK_LIST)
                                        .answerType(AnswerType.TICK)
                                        .options(
                                                Arrays.stream(four.group.jahadi.Enums.Module.ShouldGive.values())
                                                        .map(itr -> new PairValue(
                                                                itr.name(),
                                                                itr.getFaTranslate()
                                                        ))
                                                        .collect(Collectors.toList())
                                        )
                                        .required(false)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .question("ADD")
                                        .questionType(QuestionType.SIMPLE)
                                        .answerType(AnswerType.NUMBER)
                                        .required(false)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .question("عینک آفتابی")
                                        .questionType(QuestionType.CHECK_LIST)
                                        .answerType(AnswerType.TICK)
                                        .options(
                                                Arrays.stream(four.group.jahadi.Enums.Module.ShouldGive.values())
                                                        .map(itr -> new PairValue(
                                                                itr.name(),
                                                                itr.getFaTranslate()
                                                        ))
                                                        .collect(Collectors.toList())
                                        )
                                        .required(false)
                                        .build(),
                                GroupQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.GROUP)
                                        .sectionTitle("عینک ساختنی")
                                        .questions(
                                                List.of(
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(new ObjectId())
                                                                .question("نام تجویز کننده")
                                                                .questionType(QuestionType.SIMPLE)
                                                                .answerType(AnswerType.TEXT)
                                                                .required(false)
                                                                .build()
                                                )
                                        )
                                        .build(),
                                GroupQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.GROUP)
                                        .sectionTitle("دور")
                                        .questions(
                                                List.of(
                                                        TableQuestion
                                                                .builder()
                                                                .id(new ObjectId())
                                                                .required(false)
                                                                .questionType(QuestionType.TABLE)
                                                                .headers(List.of("...", "+/-", "SPH", "CYL", "VA"))
                                                                .firstColumn(List.of("OD", "OS"))
                                                                .rowsCount(2)
                                                                .answerType(AnswerType.TEXT)
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(new ObjectId())
                                                                .question("PD")
                                                                .questionType(QuestionType.SIMPLE)
                                                                .answerType(AnswerType.TEXT)
                                                                .required(false)
                                                                .build()
                                                )
                                        )
                                        .build(),
                                GroupQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .questionType(QuestionType.GROUP)
                                        .sectionTitle("نزدیک")
                                        .questions(
                                                List.of(
                                                        TableQuestion
                                                                .builder()
                                                                .id(new ObjectId())
                                                                .required(false)
                                                                .questionType(QuestionType.TABLE)
                                                                .headers(List.of("...", "+/-", "SPH", "CYL", "VA"))
                                                                .firstColumn(List.of("OD", "OS"))
                                                                .rowsCount(2)
                                                                .answerType(AnswerType.DOUBLE)
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(new ObjectId())
                                                                .question("NPD")
                                                                .questionType(QuestionType.SIMPLE)
                                                                .answerType(AnswerType.NUMBER)
                                                                .required(false)
                                                                .build()
                                                )
                                        )
                                        .build()
                        )
                )
                .build();
    }

}
