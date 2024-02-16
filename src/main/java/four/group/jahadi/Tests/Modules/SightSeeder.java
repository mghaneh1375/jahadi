package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Enums.AnswerType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.ModuleQuestion;
import four.group.jahadi.Models.ModuleTableQuestion;
import four.group.jahadi.Models.SubModule;
import org.bson.types.ObjectId;

import java.util.List;

public class SightSeeder {

    public static Module seed() {

        SubModule gharbal = SubModule
                .builder()
                .id(new ObjectId())
                .name("غربال بینایی")
                .questions(
                        List.of(
                                ModuleTableQuestion
                                        .builder()
                                        .title("غربال بینایی")
                                        .headers(List.of("VA/CC", "D"))
                                        .cellLabel("/ 10")
                                        .firstColumn(List.of("OD", "OS"))
                                        .rowsCount(2)
                                        .answerType(AnswerType.DOUBLE)
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
                                ModuleQuestion
                                        .builder()
                                        .question("ADD")
                                        .answerType(AnswerType.NUMBER)
                                        .build(),
                                ModuleQuestion
                                        .builder()
                                        .answerType(AnswerType.RADIO)
                                        .options(List.of("عینک تحویل داده شد", "تجویز عینک آفتابی"))
                                        .build(),
                                ModuleTableQuestion
                                        .builder()
                                        .title("عینک ساختنی")
                                        .headers(List.of("...", "+/-", "SPH", "CYL", "VA"))
                                        .firstColumn(List.of("OD", "OS"))
                                        .rowsCount(2)
                                        .answerType(AnswerType.DOUBLE)
                                        .build(),
                                ModuleTableQuestion
                                        .builder()
                                        .title("عینک ساختنی")
                                        .headers(List.of("NPD", "PD"))
                                        .rowsCount(1)
                                        .answerType(AnswerType.DOUBLE)
                                        .build()
                        )
                )
                .build();

        SubModule innerRefer = SubModule
                .builder()
                .id(new ObjectId())
                .name("ارجاع به خارج")
                .hasExternalReferral(true)
                .build();

        SubModule externalRefer = SubModule
                .builder()
                .id(new ObjectId())
                .name("ارجاع به خارج متخصصان چشم")
                .hasExternalSpecReferral(true)
                .build();

        return Module.builder()
                .name("بینایی سنج")
                .subModules(
                        List.of(gharbal, sightRoom, innerRefer, externalRefer)
                )
                .build();
    }

}
