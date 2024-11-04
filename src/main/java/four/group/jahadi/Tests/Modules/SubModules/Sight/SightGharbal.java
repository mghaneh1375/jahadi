package four.group.jahadi.Tests.Modules.SubModules.Sight;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.Question.TableQuestion;
import four.group.jahadi.Models.SubModule;
import org.bson.types.ObjectId;

import java.util.List;

import static four.group.jahadi.Tests.Modules.ModuleSeeder.moduleIds;

public class SightGharbal {

    public static Module seed() {
        SubModule subModule = SubModule
                .builder()
                .id(new ObjectId())
                .name("غربال بینایی")
                .referTo(moduleIds.get("اتاق بینایی"))
                .isReferral(true)
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .question("شکایت اصلی بیمار")
                                        .questionType(QuestionType.SIMPLE)
                                        .answerType(AnswerType.TEXT)
                                        .required(false)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .question("مشکل دید دور یا نزدیک")
                                        .questionType(QuestionType.SIMPLE)
                                        .answerType(AnswerType.TEXT)
                                        .required(false)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .question("سابقه بیماری زمینه ای مخصوصا فشار خون و دیابت، سوزش ، خارش یا قرمزی چشم")
                                        .questionType(QuestionType.SIMPLE)
                                        .answerType(AnswerType.TEXT)
                                        .required(false)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .question("سابقه عمل های مربوط به چشم از جمله آب مروارید")
                                        .questionType(QuestionType.SIMPLE)
                                        .answerType(AnswerType.TEXT)
                                        .required(false)
                                        .build(),
                                TableQuestion
                                        .builder()
                                        .id(new ObjectId())
                                        .required(false)
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
                                        .required(false)
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
                .icon("icon-disability")
                .tabName("توان بخشی")
                .subModules(List.of(subModule))
                .build();
    }

}
