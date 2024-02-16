package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Enums.AnswerType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.ModuleQuestion;
import four.group.jahadi.Models.SubModule;
import org.bson.types.ObjectId;

import java.util.List;

public class GeneralSeeder {

    public static Module seed() {

        return Module.builder()
                .name("پزشک عمومی")
                .subModules(
                        List.of(
                                SubModule
                                        .builder()
                                        .id(new ObjectId())
                                        .name("ویزیت و تشخیص")
                                        .questions(
                                                List.of(
                                                        ModuleQuestion
                                                                .builder()
                                                                .question("توضیحات")
                                                                .answerType(AnswerType.LONG_TEXT)
                                                                .build()
                                                )
                                        )
                                        .build(),
                                SubModule
                                        .builder()
                                        .id(new ObjectId())
                                        .name("ارجاع به خارج")
                                        .hasExternalReferral(true)
                                        .build(),
                                SubModule
                                        .builder()
                                        .id(new ObjectId())
                                        .name("ارجاع به داخل")
                                        .hasInternalReferral(true)
                                        .build(),
                                SubModule
                                        .builder()
                                        .id(new ObjectId())
                                        .name("خدمات پاراکلینیک")
                                        .canSuggestParaClinic(true)
                                        .build()
                        )
                )
                .build();

    }

}
