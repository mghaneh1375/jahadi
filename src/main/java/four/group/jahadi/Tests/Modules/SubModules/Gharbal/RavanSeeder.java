package four.group.jahadi.Tests.Modules.SubModules.Gharbal;

import four.group.jahadi.Enums.MarriageStatus;
import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.HaveOrNot;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Enums.Module.RavanAnswers;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RavanSeeder {

    public static SubModule make() {

        HashMap<String, Integer> marks = new HashMap<>();
        Arrays.stream(
                RavanAnswers.values()
        ).forEach(ravanAnswer -> marks.put(ravanAnswer.name(), ravanAnswer.getPoint()));

        return SubModule
                .builder()
                .id(new ObjectId())
                .name("غربال روان")
                .isReferral(true)
                .questions(List.of(
                        SimpleQuestion
                                .builder()
                                .questionType(QuestionType.SIMPLE)
                                .id(new ObjectId())
                                .answerType(AnswerType.SELECT)
                                .options(
                                        Arrays.stream(MarriageStatus.values()).map(
                                                itr -> new PairValue(itr.name(), itr.getFaTranslate())
                                        ).collect(Collectors.toList())
                                )
                                .question("وضعیت تاهل")
                                .required(true)
                                .build(),
                        CheckListGroupQuestion
                                .builder()
                                .questionType(QuestionType.CHECK_LIST)
                                .markable(true)
                                .id(new ObjectId())
                                .marks(marks)
                                .options(
                                        Arrays.stream(RavanAnswers.values()).map(
                                                itr -> new PairValue(itr.name(), itr.getFaTranslate())
                                        ).collect(Collectors.toList())
                                )
                                .questions(List.of(
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(true)
                                                .question("در یک ماه گذشته چقدر در خوابیدن مشکل داشته اید؟ یا اینکه در یک ماه گذشته اتفاق افتاده است که از خواب بیدار شوید و نتوانید بخوابید؟")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(true)
                                                .question("در یک ماه گذشته چقدر احساس دلشوره، اضطراب و استرس داشته اید؟")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(true)
                                                .question("در یک ماه گذشته چقدر احساس غم، ناامیدی و ناکارآمدی کرده اید؟")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(true)
                                                .question("در یک ماه گذشته چقدر رفتاری (مانند پرخاشگری) را انجام داده اید که دچار پشیمانی شوید؟")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(true)
                                                .question("در یک ماه گذشته چقدر مسائل شخصی و خانوادگی، شغل (یا حوزه های مهم دیگر زندگیتان) شما را دچار مشکل کرده است؟")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(true)
                                                .question("در یک ماه گذشته چقدر در انجام دادن کارهای روزمره شک کرده اید و مجبور به تکرار آنها شده اید؟ (مثلا شیرگاز را چندبار چک کنید، دستتان را بشویید، قفل بودن در را چندین بار بررسی کنید و ...)")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(true)
                                                .question("آیا در خورد و خوراک و تغذیه مشکلی دارید؟ (بی میلی به غذا / کم اشتهایی، پراشتهایی، استفراغ عمدی و ...)")
                                                .build()
                                ))
                                .build(),
                        SimpleQuestion
                                .builder()
                                .questionType(QuestionType.SIMPLE)
                                .id(new ObjectId())
                                .answerType(AnswerType.LONG_TEXT)
                                .question("توضیحات تکمیلی")
                                .build(),
                        SimpleQuestion
                                .builder()
                                .questionType(QuestionType.SIMPLE)
                                .id(new ObjectId())
                                .answerType(AnswerType.TEXT)
                                .required(true)
                                .question("باتوجه به میزان مصرف مواد توسط اطرافیان، چقدر خود را در معرض آن می دانید؟")
                                .build(),
                        SimpleQuestion
                                .builder()
                                .questionType(QuestionType.SIMPLE)
                                .id(new ObjectId())
                                .answerType(AnswerType.TICK)
                                .options(
                                        Arrays.stream(HaveOrNot.values()).map(
                                                itr -> new PairValue(itr.name(), itr.getFaTranslate())
                                        ).collect(Collectors.toList())
                                )
                                .question("نیاز به روانشناس")
                                .required(true)
                                .build(),
                        SimpleQuestion
                                .builder()
                                .questionType(QuestionType.SIMPLE)
                                .id(new ObjectId())
                                .answerType(AnswerType.TICK)
                                .options(
                                        Arrays.stream(HaveOrNot.values()).map(
                                                itr -> new PairValue(itr.name(), itr.getFaTranslate())
                                        ).collect(Collectors.toList())
                                )
                                .question("نیاز به پیشگیری از اعتیاد؟")
                                .required(true)
                                .build(),
                        CheckListGroupQuestion
                                .builder()
                                .questionType(QuestionType.CHECK_LIST)
                                .markable(true)
                                .id(new ObjectId())
                                .options(
                                        Arrays.stream(RavanAnswers.values()).map(
                                                itr -> new PairValue(itr.name(), itr.getFaTranslate())
                                        ).collect(Collectors.toList())
                                )
                                .marks(marks)
                                .questions(List.of(
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(true)
                                                .question("آیا در یک ماه گذشته در رابطه خود احساس تنهایی داشته اید و رابطه شما راضی کننده نبوده است؟")
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .id(new ObjectId())
                                                .questionType(QuestionType.SIMPLE)
                                                .answerType(AnswerType.TICK)
                                                .required(true)
                                                .question("در یک ماه گذشته چقدر با همسر خود به مشکل خورده اید و دعوا کرده اید؟")
                                                .build()
                                ))
                                .build(),
                        SimpleQuestion
                                .builder()
                                .questionType(QuestionType.SIMPLE)
                                .id(new ObjectId())
                                .answerType(AnswerType.LONG_TEXT)
                                .question("توضیحات تکمیلی")
                                .build()
                ))
                .build();
    }

}
