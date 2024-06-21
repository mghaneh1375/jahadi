package four.group.jahadi.Tests.Modules.SubModules.Sight;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.TableQuestion;
import four.group.jahadi.Models.SubModule;
import org.bson.types.ObjectId;

import java.util.List;

public class SightGharbal {

    public static SubModule make() {
        return SubModule
                .builder()
                .id(new ObjectId())
                .name("غربال بینایی")
                .questions(
                        List.of(
                                TableQuestion
                                        .builder()
                                        .required(true)
                                        .questionType(QuestionType.TABLE)
                                        .title("غربال بینایی")
                                        .headers(List.of("VA/CC", "D"))
                                        .cellLabel("/ 10")
                                        .firstColumn(List.of("OD", "OS"))
                                        .rowsCount(2)
                                        .answerType(AnswerType.NUMBER)
                                        .build(),
                                TableQuestion
                                        .builder()
                                        .required(true)
                                        .questionType(QuestionType.TABLE)
                                        .title("غربال بینایی")
                                        .headers(List.of("VA/SC", "D"))
                                        .cellLabel("/ 10")
                                        .firstColumn(List.of("OD", "OS"))
                                        .rowsCount(2)
                                        .answerType(AnswerType.NUMBER)
                                        .build()
                        )
                )
                .build();
    }

}
