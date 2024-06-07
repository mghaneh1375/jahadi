package four.group.jahadi.Enums.Drug;

public enum UseTime {

    ONCE_A_DAY("یکبار در روز"),
    TWICE_A_DAY("دوبار در روز"),
    THREE_TIMES_A_DAY("سه بار در روز"),
    FOUR_TIMES_A_DAY("چهار بار در روز"),
    EVERY_FOUR_HOUR("هر چهار ساعت"),
    EVERY_SIX_HOUR("هر شش ساعت"),
    EVERY_EIGHT_HOUR("هر هشت ساعت"),
    EVERY_TWELVE_HOUR("هر دوازده ساعت"),
    EVERY_DAY("هر 24 ساعت"),
    EVERY_OTHER_DAY("یک روز در میان"),
    BEFORE_SLEEP("قبل از خواب (شب ها)"),
    EVERY_MORNING("هر روز صبح"),
    ONCE_A_WEEK("یکبار در هفته"),
    ONCE_EVERY_TWO_WEEK("دو هفته ای یکبار"),
    ONCE_EVERY_THREE_WEEK("سه هفته ای یکبار"),
    ONCE_A_MONTH("ماهی یکبار"), ONCE_A_YEAR("سالی یکبار"),
    IF_NEEDED("در صورت نیاز"), ACCORDING_TO_MANUAL("طبق دستور");

    String faTranslate;

    UseTime(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
