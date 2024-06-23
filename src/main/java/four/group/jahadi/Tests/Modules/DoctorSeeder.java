package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;

import java.util.List;

import static four.group.jahadi.Tests.Modules.ModuleSeeder.commonSubModules;

public class DoctorSeeder {

    public static List<Module> seed() {

        return List.of(
                Module
                        .builder()
                        .tabName("پزشک")
                        .icon("")
                        .isReferral(true)
                        .name("پزشک عمومی")
                        .subModules(
                                List.of(
                                        commonSubModules.get("visit"),
//                                        commonSubModules.get("drug"),
                                        commonSubModules.get("externalReferral"),
                                        commonSubModules.get("paraClinic")
//                                        commonSubModules.get("experiment"),
                                )
                        )
                        .build(),
                Module
                        .builder()
                        .tabName("پزشک")
                        .icon("")
                        .name("آموزش پس از پزشک")
                        .subModules(
                                List.of(
                                        SubModule
                                                .builder()
                                                .name("آموزش پس از پزشک")
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
                                                .postAction("submit_in_post_doctor_table")
                                                .build()
                                )
                        )
                        .build()
        );
    }

}
