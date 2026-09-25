package four.group.jahadi.Tests.Modules.SubModules.Sight;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.Question.TableQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Tests.Modules.SubModules.Helper;
import org.bson.types.ObjectId;

import java.util.List;

import static four.group.jahadi.Tests.Modules.ModuleSeeder.moduleIds;

public class SightGharbal {

    public static Module seed() {
        String moduleName = "غربالگری دوم بینایی";
        String subModuleName = "غربالگری بینایی";
        SubModule oldSubModule = Helper.findSubModule(moduleName, subModuleName);
        Question question1 = Helper.findQuestionInSubModule(oldSubModule, "شکایت اصلی بیمار");
        Question question2 = Helper.findQuestionInSubModule(oldSubModule, "مشکل دید دور یا نزدیک");
        Question question3 = Helper.findQuestionInSubModule(oldSubModule, "سابقه بیماری زمینه ای مخصوصا فشار خون و دیابت، سوزش ، خارش یا قرمزی چشم");
        Question question4 = Helper.findQuestionInSubModule(oldSubModule, "سابقه عمل های مربوط به چشم از جمله آب مروارید");
        Question tableQuestion1 = Helper.findQuestionInSubModule(oldSubModule, List.of("VA/CC", "D"));
        Question tableQuestion2 = Helper.findQuestionInSubModule(oldSubModule, List.of("VA/SC", "D"));

        SubModule subModule = SubModule
                .builder()
                .id(oldSubModule == null ? new ObjectId() : oldSubModule.getId())
                .name(subModuleName)
                .referTo(moduleIds.get("اتاق بینایی"))
                .isReferral(true)
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .id(question1 == null ? new ObjectId() : question1.getId())
                                        .question("شکایت اصلی بیمار")
                                        .questionType(QuestionType.SIMPLE)
                                        .answerType(AnswerType.TEXT)
                                        .required(false)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(question2 == null ? new ObjectId() : question2.getId())
                                        .question("مشکل دید دور یا نزدیک")
                                        .questionType(QuestionType.SIMPLE)
                                        .answerType(AnswerType.TEXT)
                                        .required(false)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(question3 == null ? new ObjectId() : question3.getId())
                                        .question("سابقه بیماری زمینه ای مخصوصا فشار خون و دیابت، سوزش ، خارش یا قرمزی چشم")
                                        .questionType(QuestionType.SIMPLE)
                                        .answerType(AnswerType.TEXT)
                                        .required(false)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(question4 == null ? new ObjectId() : question4.getId())
                                        .question("سابقه عمل های مربوط به چشم از جمله آب مروارید")
                                        .questionType(QuestionType.SIMPLE)
                                        .answerType(AnswerType.TEXT)
                                        .required(false)
                                        .build(),
                                TableQuestion
                                        .builder()
                                        .id(tableQuestion1 == null ? new ObjectId() : tableQuestion1.getId())
                                        .required(false)
                                        .questionType(QuestionType.TABLE)
                                        .title("غربالگری بینایی")
                                        .headers(List.of("VA/CC", "D"))
                                        .cellLabel("/ 10")
                                        .firstColumn(List.of("OD", "OS"))
                                        .rowsCount(2)
                                        .answerType(AnswerType.TEXT)
                                        .build(),
                                TableQuestion
                                        .builder()
                                        .required(false)
                                        .id(tableQuestion2 == null ? new ObjectId() : tableQuestion2.getId())
                                        .questionType(QuestionType.TABLE)
                                        .title("غربالگری بینایی")
                                        .headers(List.of("VA/SC", "D"))
                                        .cellLabel("/ 10")
                                        .firstColumn(List.of("OD", "OS"))
                                        .rowsCount(2)
                                        .answerType(AnswerType.TEXT)
                                        .build()
                        )
                )
                .build();

        return Module
                .builder()
                .name(moduleName)
                .icon("icon-disability")
                .tabName("توان بخشی")
                .subModules(List.of(subModule))
                .build();
    }

}
