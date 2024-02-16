package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Enums.AnswerType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.ModuleQuestion;
import four.group.jahadi.Models.ModuleTableQuestion;
import four.group.jahadi.Models.SubModule;
import org.bson.types.ObjectId;

import java.util.List;

public class Audiologists {

    public static Module seed() {

        SubModule room1 = SubModule
                .builder()
                .id(new ObjectId())
                .name("اتاق شنوایی 1")
                .questions(
                        List.of(
                                ModuleQuestion
                                        .builder()
                                        .question("نتیجه اتوسکوپی")
                                        .answerType(AnswerType.TEXT)
                                        .build(),
                                ModuleQuestion
                                        .builder()
                                        .question("ویزیت")
                                        .answerType(AnswerType.TEXT)
                                        .build(),
                                ModuleTableQuestion
                                        .builder()
                                        .title("اطلاعات")
                                        .headers(List.of("...", "500", "1000", "2000", "3000", "4000", "6000", "8000"))
                                        .firstColumn(List.of("TH (right)", "TH (left)"))
                                        .rowsCount(2)
                                        .answerType(AnswerType.NUMBER)
                                        .build(),
                                ModuleTableQuestion
                                        .builder()
                                        .title("اطلاعات")
                                        .headers(List.of("L", "R"))
                                        .rowsCount(1)
                                        .answerType(AnswerType.NUMBER)
                                        .build()
                        )
                )
                .build();

        SubModule room2 = SubModule
                .builder()
                .id(new ObjectId())
                .name("اتاق شنوایی 2")
                .questions(
                        List.of(
                                ModuleTableQuestion
                                        .builder()
                                        .title("Rinne")
                                        .headers(List.of("Freq", "L", "R"))
                                        .firstColumn(List.of("250", "500", "1000"))
                                        .rowsCount(3)
                                        .answerType(AnswerType.NUMBER)
                                        .build(),
                                ModuleTableQuestion
                                        .builder()
                                        .title("Weber")
                                        .headers(List.of("Freq", ""))
                                        .firstColumn(List.of("250", "500", "1000"))
                                        .rowsCount(3)
                                        .answerType(AnswerType.NUMBER)
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
                .name("شنوایی سنج")
                .subModules(
                        List.of(room1, room2, innerRefer, externalRefer)
                )
                .build();
    }
}
