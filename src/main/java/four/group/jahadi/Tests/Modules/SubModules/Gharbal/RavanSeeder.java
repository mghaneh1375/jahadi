package four.group.jahadi.Tests.Modules.SubModules.Gharbal;

import four.group.jahadi.Enums.MarriageStatus;
import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.HaveOrNot;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Enums.Module.RavanAnswers;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Tests.Modules.SubModules.Helper;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RavanSeeder {

    public static SubModule make(String moduleName, ObjectId referToOid) {
        String subModuleName = "غربالگری روان";
        SubModule oldSubModule = Helper.findSubModule(moduleName, subModuleName);

        HashMap<String, Integer> marks = new HashMap<>();
        Arrays.stream(
                RavanAnswers.values()
        ).forEach(ravanAnswer -> marks.put(ravanAnswer.name(), ravanAnswer.getPoint()));

        Question mainQuestion1 = oldSubModule == null ? null : oldSubModule.getQuestions().get(0);
        Question mainQuestion2 = oldSubModule == null ? null : oldSubModule.getQuestions().get(1);
        Question mainQuestion3 = Helper.findQuestionInSubModule(oldSubModule, "وضعیت تاهل");
        Question mainQuestion4 = Helper.findQuestionInSubModule(oldSubModule, "توضیحات تکمیلی");
        Question mainQuestion5 = Helper.findQuestionInSubModule(oldSubModule, "موارد مداخله در بحران");

        return SubModule
                .builder()
                .id(oldSubModule == null ? new ObjectId() : oldSubModule.getId())
                .name(subModuleName)
                .referTo(referToOid)
                .isReferral(true)
                .questions(List.of(
                        CheckListGroupQuestion
                                .builder()
                                .questionType(QuestionType.CHECK_LIST)
                                .markable(true)
                                .id(mainQuestion1 == null ? new ObjectId() : mainQuestion1.getId())
                                .marks(marks)
                                .options(
                                        Arrays.stream(RavanAnswers.values()).map(
                                                itr -> new PairValue(itr.name(), itr.getFaTranslate())
                                        ).collect(Collectors.toList())
                                )
                                .questions(List.of(
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "در یک ماه گذشته چقدر در خوابیدن مشکل داشته اید؟ یا اینکه در یک ماه گذشته اتفاق افتاده است که از خواب بیدار شوید و نتوانید بخوابید؟"))
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .question("در یک ماه گذشته چقدر در خوابیدن مشکل داشته اید؟ یا اینکه در یک ماه گذشته اتفاق افتاده است که از خواب بیدار شوید و نتوانید بخوابید؟")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "در یک ماه گذشته چقدر احساس دلشوره، اضطراب و استرس داشته اید؟"))
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .question("در یک ماه گذشته چقدر احساس دلشوره، اضطراب و استرس داشته اید؟")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "در یک ماه گذشته چقدر احساس غم، ناامیدی و ناکارآمدی کرده اید؟"))
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .question("در یک ماه گذشته چقدر احساس غم، ناامیدی و ناکارآمدی کرده اید؟")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "در یک ماه گذشته چقدر رفتاری (مانند پرخاشگری) را انجام داده اید که دچار پشیمانی شوید؟"))
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .question("در یک ماه گذشته چقدر رفتاری (مانند پرخاشگری) را انجام داده اید که دچار پشیمانی شوید؟")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "در یک ماه گذشته چقدر مسائل شخصی و خانوادگی، شغل (یا حوزه های مهم دیگر زندگیتان) شما را دچار مشکل کرده است؟"))
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .question("در یک ماه گذشته چقدر مسائل شخصی و خانوادگی، شغل (یا حوزه های مهم دیگر زندگیتان) شما را دچار مشکل کرده است؟")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "در یک ماه گذشته چقدر در انجام دادن کارهای روزمره شک کرده اید و مجبور به تکرار آنها شده اید؟ (مثلا شیرگاز را چندبار چک کنید، دستتان را بشویید، قفل بودن در را چندین بار بررسی کنید و ...)"))
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .question("در یک ماه گذشته چقدر در انجام دادن کارهای روزمره شک کرده اید و مجبور به تکرار آنها شده اید؟ (مثلا شیرگاز را چندبار چک کنید، دستتان را بشویید، قفل بودن در را چندین بار بررسی کنید و ...)")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "آیا در خورد و خوراک و تغذیه مشکلی دارید؟ (بی میلی به غذا / کم اشتهایی، پراشتهایی، استفراغ عمدی و ...)"))
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .question("آیا در خورد و خوراک و تغذیه مشکلی دارید؟ (بی میلی به غذا / کم اشتهایی، پراشتهایی، استفراغ عمدی و ...)")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "باتوجه به میزان مصرف مواد توسط اطرافیان، چقدر خود را در معرض آن می دانید؟"))
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .question("باتوجه به میزان مصرف مواد توسط اطرافیان، چقدر خود را در معرض آن می دانید؟")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "آیا در یک ماه گذشته در رابطه خود احساس تنهایی داشته اید و رابطه شما راضی کننده نبوده است؟"))
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .question("آیا در یک ماه گذشته در رابطه خود احساس تنهایی داشته اید و رابطه شما راضی کننده نبوده است؟")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion1, "در یک ماه گذشته چقدر با همسر خود به مشکل خورده اید و دعوا کرده اید؟"))
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(false)
                                                .question("در یک ماه گذشته چقدر با همسر خود به مشکل خورده اید و دعوا کرده اید؟")
                                                .build()
                                ))
                                .build(),
                        CheckListGroupQuestion
                                .builder()
                                .questionType(QuestionType.CHECK_LIST)
                                .id(mainQuestion2 == null ? new ObjectId() : mainQuestion2.getId())
                                .options(
                                        Arrays.stream(HaveOrNot.values()).map(
                                                itr -> new PairValue(itr.name(), itr.getFaTranslate())
                                        ).collect(Collectors.toList())
                                )
                                .questions(List.of(
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion2, "نیاز به مداخله بالینی"))
                                                .answerType(AnswerType.TICK)
                                                .question("نیاز به مداخله بالینی")
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion2, "نیاز به روانشناس"))
                                                .answerType(AnswerType.TICK)
                                                .question("نیاز به روانشناس")
                                                .required(false)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .questionType(QuestionType.SIMPLE)
                                                .id(Helper.findSimpleQuestionInList((CheckListGroupQuestion) mainQuestion2, "نیاز به پیشگیری از اعتیاد؟"))
                                                .answerType(AnswerType.TICK)
                                                .question("نیاز به پیشگیری از اعتیاد؟")
                                                .required(false)
                                                .build()
                                ))
                                .build(),
                        SimpleQuestion
                                .builder()
                                .questionType(QuestionType.SIMPLE)
                                .id(mainQuestion3 == null ? new ObjectId() : mainQuestion3.getId())
                                .answerType(AnswerType.SELECT)
                                .options(
                                        Arrays.stream(MarriageStatus.values()).map(
                                                itr -> new PairValue(itr.name(), itr.getFaTranslate())
                                        ).collect(Collectors.toList())
                                )
                                .question("وضعیت تاهل")
                                .required(false)
                                .build(),
//                        SimpleQuestion
//                                .builder()
//                                .questionType(QuestionType.SIMPLE)
//                                .id(new ObjectId())
//                                .answerType(AnswerType.DATE)
//                                .question("تاریخ")
//                                .build(),
                        SimpleQuestion
                                .builder()
                                .questionType(QuestionType.SIMPLE)
                                .id(mainQuestion4 == null ? new ObjectId() : mainQuestion4.getId())
                                .answerType(AnswerType.LONG_TEXT)
                                .question("توضیحات تکمیلی")
                                .required(false)
                                .build(),
                        SimpleQuestion
                                .builder()
                                .questionType(QuestionType.SIMPLE)
                                .id(mainQuestion5 == null ? new ObjectId() : mainQuestion5.getId())
                                .answerType(AnswerType.TEXT)
                                .required(false)
                                .question("موارد مداخله در بحران")
                                .build()
                ))
                .build();
    }

}
