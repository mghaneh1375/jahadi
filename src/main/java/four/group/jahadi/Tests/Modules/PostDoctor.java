package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.PostDoctorOptions;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;

import java.util.List;

public class PostDoctor {
    public static Module seed() {
        return Module
                .builder()
                .name("پست پزشک")
                .icon("")
                .subModules(
                        List.of(
                                SubModule
                                        .builder()
                                        .name("آموزش پس از پزشک")
                                        .questions(
                                                List.of(
                                                        SimpleQuestion
                                                                .builder()
                                                                .answerType(AnswerType.TICK)
                                                                .question("")
                                                                .required(false)
                                                                .options(
                                                                        List.of(
                                                                                new PairValue(
                                                                                        PostDoctorOptions.TUTORIAL.name(),
                                                                                        PostDoctorOptions.TUTORIAL.getFaTranslate()
                                                                                ),
                                                                                new PairValue(
                                                                                        PostDoctorOptions.MAG.name(),
                                                                                        PostDoctorOptions.MAG.getFaTranslate()
                                                                                )
                                                                        )
                                                                )
                                                                .build()
                                                )
                                        )
                                        .build()
                        )
                )
                .build();
    }
}
