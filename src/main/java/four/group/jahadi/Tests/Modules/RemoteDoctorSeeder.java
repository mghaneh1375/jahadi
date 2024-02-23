package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;

import java.util.List;

import static four.group.jahadi.Tests.Modules.ModuleSeeder.commonSubModules;

public class RemoteDoctorSeeder {

    public static Module seed() {

        return Module
                .builder()
                .name("دورا پزشک")
                .icon("")
                .subModules(
                        List.of(
                                commonSubModules.get("visit"),
                                commonSubModules.get("experiment"),
                                commonSubModules.get("externalReferral"),
                                commonSubModules.get("internalReferral"),
                                commonSubModules.get("remoteReferral"),
                                commonSubModules.get("paraClinic"),
                                SubModule
                                        .builder()
                                        .name("آموزش پس از پزشک")
                                        .questions(
                                                List.of(
                                                        SimpleQuestion
                                                                .builder()
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
                .build();
    }

}
