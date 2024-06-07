package four.group.jahadi.Enums.Drug;

public enum AmountOfUse {

    ONE_TAB("یک عدد Tab/cap"),
    TWO_TAB("دو عدد Tab/cap"),
    THREE_TAB("سه عدد Tab/cap"),
    FOUR_TAB("چهار عدد Tab/cap"),
    HALF_TAB("نصف Tab/cap"),
    QUARTER_TAB("یک چهارم Tab/cap"),
    QUARTER_INJ("یک چهارم INJ"),
    THIRD_INJ("یک سوم INJ"),
    TWO_THIRD_INJ("دو سوم INJ"),
    ONE_INJ("یک عدد INJ"),
    HALF_PAST_ONE_INJ("یک و نیم INJ"),
    TWO_INJ("دو عدد INJ"),
    HALF_CC_SYRUP("نیم سی سی syrup/susp"),
    ONE_CC_SYRUP("یک سی سی syrup/susp"),
    HALF_PAST_ONE_CC_SYRUP("یک و نیم سی سی syrup/susp"),
    TWO_CC_SYRUP("دو سی سی syrup/susp"),
    HALF_PAST_TWO_CC_SYRUP("دو و نیم سی سی syrup/susp (یک قاشق چای خوری)"),
    THREE_CC_SYRUP("سه سی سی syrup/susp"),
    HALF_PAST_THREE_CC_SYRUP("سه و نیم سی سی syrup/susp"),
    FOUR_CC_SYRUP("چهار سی سی syrup/susp"),
    HALF_PAST_FOUR_CC_SYRUP("چهار و نیم سی سی syrup/susp"),
    FIVE_CC_SYRUP("پنچ سی سی syrup/susp (یک قاشق مرباخوری)"),
    HALF_PAST_SEVEN_CC_SYRUP("هفت و نیم سی سی syrup/susp"),
    TEN_CC_SYRUP("10 سی سی syrup/susp (یک قاشق غذاخوری)"),
    FIFTEEN_CC_SYRUP("15 سی سی syrup/susp (یک قاشق سوپ خوری)"),
    ;

    String faTranslate;

    AmountOfUse(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
