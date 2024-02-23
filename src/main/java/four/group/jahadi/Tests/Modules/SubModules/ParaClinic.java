package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.LimitedParaClinic;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;

import java.util.List;

public class ParaClinic {

    public static SubModule make() {
        return SubModule
                .builder()
                .name("خدمات پاراکلینیک")
                .questions(
                        List.of(
                                SimpleQuestion
                                        .builder()
                                        .required(true)
                                        .answerType(AnswerType.MULTI_SELECT)
                                        .options(
                                                List.of(
                                                        new PairValue(
                                                                LimitedParaClinic.ECG.name(),
                                                                LimitedParaClinic.ECG.getFaTranslate()
                                                        ),
                                                        new PairValue(
                                                                LimitedParaClinic.BANDAGE.name(),
                                                                LimitedParaClinic.BANDAGE.getFaTranslate()
                                                        ),
                                                        new PairValue(
                                                                LimitedParaClinic.STITCH.name(),
                                                                LimitedParaClinic.STITCH.getFaTranslate()
                                                        ),
                                                        new PairValue(
                                                                LimitedParaClinic.SOUNDING.name(),
                                                                LimitedParaClinic.SOUNDING.getFaTranslate()
                                                        )
                                                )
                                        )
                                        .build(),
                                SimpleQuestion
                                        .builder()
                                        .answerType(AnswerType.LONG_TEXT)
                                        .question("توضیحات")
                                        .required(true)
                                        .build()
                        )
                )
                .build();
    }

}
