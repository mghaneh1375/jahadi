package four.group.jahadi.Tests.Modules;

import four.group.jahadi.Enums.Module.AllParaClinic;
import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;

import java.util.List;

public class ParaClinic {
    public static Module seed() {
        return Module
                .builder()
                .name("پاراکلینیک")
                .icon("")
                .subModules(
                        List.of(
                                SubModule
                                        .builder()
                                        .name("خدمات پاراکلینیک")
                                        .questions(
                                                List.of(
                                                        SimpleQuestion
                                                                .builder()
                                                                .questionType(QuestionType.SIMPLE)
                                                                .question("نوع خدمت")
                                                                .answerType(AnswerType.UPLOAD_TIC)
                                                                .options(
                                                                        List.of(
                                                                                new PairValue(
                                                                                        AllParaClinic.ECG.name(),
                                                                                        AllParaClinic.ECG.getFaTranslate()
                                                                                ),
                                                                                new PairValue(
                                                                                        AllParaClinic.BANDAGE.name(),
                                                                                        AllParaClinic.BANDAGE.getFaTranslate()
                                                                                ),
                                                                                new PairValue(
                                                                                        AllParaClinic.STITCH.name(),
                                                                                        AllParaClinic.STITCH.getFaTranslate()
                                                                                ),
                                                                                new PairValue(
                                                                                        AllParaClinic.SOUNDING.name(),
                                                                                        AllParaClinic.SOUNDING.getFaTranslate()
                                                                                ),
                                                                                new PairValue(
                                                                                        AllParaClinic.DRUG_IV.name(),
                                                                                        AllParaClinic.DRUG_IV.getFaTranslate()
                                                                                ),
                                                                                new PairValue(
                                                                                        AllParaClinic.IV.name(),
                                                                                        AllParaClinic.IV.getFaTranslate()
                                                                                ),
                                                                                new PairValue(
                                                                                        AllParaClinic.IM.name(),
                                                                                        AllParaClinic.IM.getFaTranslate()
                                                                                )
                                                                        )
                                                                )
                                                                .required(true)
                                                                .build()
                                                )
                                        )
                                        .build(),
                                SubModule
                                        .builder()
                                        .name("اقدامات احیا")
                                        .questions(
                                                List.of(
                                                        SimpleQuestion
                                                                .builder()
                                                                .questionType(QuestionType.SIMPLE)
                                                                .answerType(AnswerType.LONG_TEXT)
                                                                .required(true)
                                                                .question("توضیحات")
                                                                .build(),
                                                        SimpleQuestion
                                                                .builder()
                                                                .questionType(QuestionType.SIMPLE)
                                                                .answerType(AnswerType.LONG_TEXT)
                                                                .required(true)
                                                                .question("افرادی که حضور داشتند")
                                                                .build()
                                                )
                                        )
                                        .build()
                        )
                )
                .build();
    }
}
