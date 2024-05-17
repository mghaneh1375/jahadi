package four.group.jahadi.Tests.Modules.SubModules.Gharbal;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.YesOrNo;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Utility.PairValue;

import java.util.List;

public class Mama {
    public static SubModule make() {
        return SubModule
                .builder()
                .name("غربال مامایی")
                .questions(List.of(
                        CheckListGroupQuestion
                                .builder()
                                .sectionTitle("غربالگری سرطان پستان")
                                .options(List.of(
                                        new PairValue(
                                                YesOrNo.YES.name(),
                                                YesOrNo.YES.getFaTranslate()
                                        ),
                                        new PairValue(
                                                YesOrNo.NO.name(),
                                                YesOrNo.NO.getFaTranslate()
                                        )
                                ))
                                .questions(List.of(
                                        SimpleQuestion
                                                .builder()
                                                .question("سابقه ابتلا به سرطان در فرد، سابقه ابتلا به سرطان")
                                                .answerType(AnswerType.TICK)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .question("سابقه کم شنوایی یا ناشنوایی در خانواده")
                                                .answerType(AnswerType.TICK)
                                                .build(),
                                        SimpleQuestion
                                                .builder()
                                                .question("سابقه بستری در بیمارستان زمان نوزادی و زردی و ازدواج فامیلی والدین")
                                                .answerType(AnswerType.TICK)
                                                .build()
                                ))
                                .build()
                ))
                .build();
    }
}
