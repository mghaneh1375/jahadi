package four.group.jahadi.Tests.Modules.SubModules.Sight;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.Glass;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.GroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.Question.TableQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.List;

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
                                        .question("ADD")
                                        .answerType(AnswerType.NUMBER)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .answerType(AnswerType.TICK)
                                        .options(
                                                List.of(
                                                        new PairValue(
                                                                Glass.GIVE.name(),
                                                                Glass.GIVE.getFaTranslate()
                                                        ),
                                                        new PairValue(
                                                                Glass.SHOULD_GIVE_SUN_GLASS.name(),
                                                                Glass.SHOULD_GIVE_SUN_GLASS.getFaTranslate()
                                                        )
                                                )
                                        )
                                        .build(),
                                GroupQuestion
                                        .builder()
                                        .questionType(QuestionType.GROUP)
                                        .sectionTitle("عینک ساختنی")
                                        .questions(
                                                List.of(
                                                        TableQuestion
                                                                .builder()
                                                                .required(true)
                                                                .questionType(QuestionType.TABLE)
                                                                .headers(List.of("...", "+/-", "SPH", "CYL", "VA"))
                                                                .firstColumn(List.of("OD", "OS"))
                                                                .rowsCount(2)
                                                                .answerType(AnswerType.DOUBLE)
                                                                .build(),
                                                        TableQuestion
                                                                .builder()
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
