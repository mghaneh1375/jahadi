package four.group.jahadi.Enums;

public enum Insurance {

    TAKMIL("تکمیلی"), MILITARY("نیروهای مسلح"), TAMIN("تامین اجتماعی"), SALAMAT("سلامت"), NONE("فاقد بیمه");

    private final String faTranslate;
    Insurance(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }

    public String getName() {
        return name().toLowerCase();
    }

}
