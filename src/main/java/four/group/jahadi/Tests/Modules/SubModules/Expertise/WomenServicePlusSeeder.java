package four.group.jahadi.Tests.Modules.SubModules.Expertise;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Enums.Module.YesOrNo;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Tests.Modules.SubModules.Helper;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WomenServicePlusSeeder {
    public static SubModule make(String moduleName) {
        String subModuleName = moduleName.equals("مامایی") ? "مامایی" : "خدمات تخصصی زنان";
        SubModule oldSubModule = Helper.findSubModule(moduleName, subModuleName);
        Question mainQuestion1 = Helper.findQuestionInSubModule(oldSubModule, "");

        Question mainQuestion2 = moduleName.equals("مامایی")
                ? Helper.findQuestionInSubModule(oldSubModule, "بارداری")
                : Helper.findQuestionInSubModule(
                Helper.findSubModule("غربالگری پایه", "غربالگری مامایی"),
                "بارداری"
        );

        return SubModule
                .builder()
                .id(oldSubModule == null ? new ObjectId() : oldSubModule.getId())
                .name(subModuleName)
                .questions(
                        List.of(
                                CheckListGroupQuestion
                                        .builder()
                                        .id(mainQuestion1 == null ? new ObjectId() : mainQuestion1.getId())
                                        .questionType(QuestionType.CHECK_LIST)
                                        .sectionTitle("")
                                        .options(
                                                Arrays.stream(four.group.jahadi.Enums.Module.DoneOrNot.values())
                                                        .map(itr -> new PairValue(
                                                                itr.name(),
                                                                itr.getFaTranslate()
                                                        ))
                                                        .collect(Collectors.toList())
                                        )
                                        .questions(
                                                moduleName.equals("مامایی") ?
                                                        Arrays.stream(four.group.jahadi.Enums.Module.Mama.values())
                                                                .map(itr ->
                                                                        SimpleQuestion
                                                                                .builder()
                                                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, itr.getFaTranslate()))
                                                                                .questionType(QuestionType.SIMPLE)
                                                                                .question(itr.getFaTranslate())
                                                                                .answerType(AnswerType.TICK)
                                                                                .required(false)
                                                                                .build()
                                                                ).collect(Collectors.toList()) :
                                                        Arrays.stream(four.group.jahadi.Enums.Module.WomenServicePlus.values())
                                                                .map(itr ->
                                                                        SimpleQuestion
                                                                                .builder()
                                                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, itr.getFaTranslate()))
                                                                                .questionType(QuestionType.SIMPLE)
                                                                                .question(itr.getFaTranslate())
                                                                                .answerType(AnswerType.TICK)
                                                                                .required(false)
                                                                                .build()
                                                                ).collect(Collectors.toList())
                                        )
                                        .canWriteReport(true)
                                        .canWriteReason(true)
                                        .canWriteSampleInfoDesc(true)
                                        .build(),
                                CheckListGroupQuestion
                                        .builder()
                                        .id(mainQuestion2 == null ? new ObjectId() : mainQuestion2.getId())
                                        .questionType(QuestionType.CHECK_LIST)
                                        .sectionTitle("بارداری")
                                        .options(List.of(
                                                new PairValue(
                                                        YesOrNo.YES.name(),
                                                        YesOrNo.YES.getFaTranslate()
                                                ),
                                                new PairValue(
                                                        YesOrNo.NO.name(),
                                                        YesOrNo.NO.getFaTranslate()
                                                )
                                        ))
                                        .questions(List.of(
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion2, "مطمئن بودن از بارداری"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("مطمئن بودن از بارداری")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion2, "مشکوک به بارداری"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("مشکوک به بارداری")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion2, "ناباروری یا نیاز به آموزش"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("ناباروری یا نیاز به آموزش")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion2, "اشکال در باروری"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("اشکال در باروری")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion2, "خونریزی و لکه بینی"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("خونریزی و لکه بینی")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion2, "تروما به شکم یا غیره"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("تروما به شکم یا غیره")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion2, "ناباروربودن اولیه"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("ناباروربودن اولیه")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion2, "ناباروربودن ثانویه"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("ناباروربودن ثانویه")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build(),
                                                SimpleQuestion
                                                        .builder()
                                                        .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion2, "بسته پروانه"))
                                                        .questionType(QuestionType.SIMPLE)
                                                        .question("بسته پروانه")
                                                        .answerType(AnswerType.TICK)
                                                        .required(false)
                                                        .build()
                                        ))
                                        .build()
                        )
                )
                .build();
    }
}
