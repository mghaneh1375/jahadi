package four.group.jahadi.Tests.Modules.SubModules.Sight;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.TableQuestion;
import four.group.jahadi.Models.SubModule;
import org.bson.types.ObjectId;

import java.util.List;

public class SightGharbal {

    public static Module seed() {
        SubModule subModule = SubModule
                .builder()
                .id(new ObjectId())
                .isReferral(true)
                .name("غربال بینایی")
                .questions(
                        List.of(
                                TableQuestion
                                        .builder()
                                        .id(new ObjectId())
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
                                        .id(new ObjectId())
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

        return Module
                .builder()
                .name("غربال دوم بینایی")
                .icon("")
                .tabName("توان بخشی")
                .subModules(List.of(subModule))
                .build();
    }

}
