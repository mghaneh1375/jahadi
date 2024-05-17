package four.group.jahadi.Enums.Module;

public enum Shepesh {
    NORMAL("نرمال"), NIT("نیت"), BALEGH("شپش بالغ");

    String faTranslate;

    Shepesh(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }

}
