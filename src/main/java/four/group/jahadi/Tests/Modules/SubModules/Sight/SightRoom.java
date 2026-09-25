package four.group.jahadi.Tests.Modules.SubModules.Sight;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.GroupQuestion;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.Question.TableQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Tests.Modules.SubModules.Helper;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static four.group.jahadi.Tests.Modules.ModuleSeeder.moduleIds;

public class SightRoom {

    public static SubModule make(String moduleName) {
        String subModuleName = "اتاق بینایی";
        SubModule oldSubModule = Helper.findSubModule(moduleName, subModuleName);
        Question question1 = Helper.findQuestionInSubModule(oldSubModule, "عینک مطالعه");
        Question question2 = Helper.findQuestionInSubModule(oldSubModule, "ADD");
        Question question3 = Helper.findQuestionInSubModule(oldSubModule, "عینک آفتابی");
        Question question4 = Helper.findQuestionInSubModule(oldSubModule, "عینک ساختنی");
        Question question5 = Helper.findQuestionInSubModule(oldSubModule, "دور");
        Question question6 = Helper.findQuestionInSubModule(oldSubModule, "نزدیک");

        return SubModule
                .builder()
                .id(oldSubModule == null ? new ObjectId() : oldSubModule.getId())
                .name(subModuleName)
                .isReferral(true)
                .referTo(moduleIds.get("متخصص چشم پزشکی"))
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .id(question1 == null ? new ObjectId() : question1.getId())
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
                                        .id(question2 == null ? new ObjectId() : question2.getId())
                                        .question("ADD")
                                        .questionType(QuestionType.SIMPLE)
                                        .answerType(AnswerType.TEXT)
                                        .required(false)
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .id(question3 == null ? new ObjectId() : question3.getId())
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
                                        .id(question4 == null ? new ObjectId() : question4.getId())
                                        .questionType(QuestionType.GROUP)
                                        .sectionTitle("عینک ساختنی")
                                        .questions(
                                                List.of(
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(Helper.findSimpleQuestionInList((GroupQuestion) question4, "نام تجویز کننده"))
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
                                        .id(question5 == null ? new ObjectId() : question5.getId())
                                        .questionType(QuestionType.GROUP)
                                        .sectionTitle("دور")
                                        .questions(
                                                List.of(
                                                        TableQuestion
                                                                .builder()
                                                                .id(Helper.findTableQuestionInList((GroupQuestion) question5))
                                                                .required(false)
                                                                .questionType(QuestionType.TABLE)
                                                                .headers(List.of("...", "+/-", "SPH", "CYL", "VA"))
                                                                .firstColumn(List.of("OD", "OS"))
                                                                .rowsCount(2)
                                                                .answerType(AnswerType.TEXT)
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(Helper.findSimpleQuestionInList((GroupQuestion) question5, "PD"))
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
                                        .id(question6 == null ? new ObjectId() : question6.getId())
                                        .questionType(QuestionType.GROUP)
                                        .sectionTitle("نزدیک")
                                        .questions(
                                                List.of(
                                                        TableQuestion
                                                                .builder()
                                                                .id(Helper.findTableQuestionInList((GroupQuestion) question6))
                                                                .required(false)
                                                                .questionType(QuestionType.TABLE)
                                                                .headers(List.of("...", "+/-", "SPH", "CYL", "VA"))
                                                                .firstColumn(List.of("OD", "OS"))
                                                                .rowsCount(2)
                                                                .answerType(AnswerType.TEXT)
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .id(Helper.findSimpleQuestionInList((GroupQuestion) question6, "NPD"))
                                                                .question("NPD")
                                                                .questionType(QuestionType.SIMPLE)
                                                                .answerType(AnswerType.TEXT)
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
