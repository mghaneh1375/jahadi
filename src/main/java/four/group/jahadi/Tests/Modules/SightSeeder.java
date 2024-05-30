package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.Glass;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.GroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.Question.TableQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.List;

import static four.group.jahadi.Tests.Modules.ModuleSeeder.commonSubModules;

public class SightSeeder {

    public static Module seed() {

        SubModule gharbal = SubModule
                .builder()
                .id(new ObjectId())
                .name("غربال دوم بینایی")
                .questions(
                        List.of(
                                TableQuestion
                                        .builder()
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

        SubModule sightRoom = SubModule
                .builder()
                .id(new ObjectId())
                .name("اتاق بینایی")
                .questions(
                        List.of(
                                GroupQuestion
                                        .builder()
                                        .questionType(QuestionType.GROUP)
                                        .sectionTitle("عینک تحویلی")
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
                                                                                )
                                                                        )
                                                                )
                                                                .build()
                                                )
                                        )
                                        .build(),
                                GroupQuestion
                                        .builder()
                                        .questionType(QuestionType.GROUP)
                                        .sectionTitle("عینک آفتابی")
                                        .questions(
                                                List.of(
                                                        SimpleQuestion
                                                                .builder()
                                                                .answerType(AnswerType.TICK)
                                                                .options(
                                                                        List.of(
                                                                                new PairValue(
                                                                                        Glass.SHOULD_GIVE_SUN_GLASS.name(),
                                                                                        Glass.SHOULD_GIVE_SUN_GLASS.getFaTranslate()
                                                                                )
                                                                        )
                                                                )
                                                                .build()
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
                                                                .questionType(QuestionType.TABLE)
                                                                .headers(List.of("...", "+/-", "SPH", "CYL", "VA"))
                                                                .firstColumn(List.of("OD", "OS"))
                                                                .rowsCount(2)
                                                                .answerType(AnswerType.DOUBLE)
                                                                .build(),
                                                        TableQuestion
                                                                .builder()
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

        SubModule externalRefer = SubModule
                .builder()
                .name("ارجاع به خارج متخصصان چشم")
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .questionType(QuestionType.SIMPLE)
                                        .required(true)
                                        .question("علت ارجاع")
                                        .answerType(AnswerType.LONG_TEXT)
                                        .build()
                        )
                )
                .build();

        return Module.builder()
                .name("بینایی سنج")
                .subModules(
                        List.of(gharbal, sightRoom, commonSubModules.get("externalReferral"), externalRefer)
                )
                .build();
    }

}
