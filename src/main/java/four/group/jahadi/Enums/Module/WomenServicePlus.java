package four.group.jahadi.Enums.Module;

public enum WomenServicePlus {
    SUPER_EXAMINATION("معاینه برست"),
    VAGINAL_EXAMINATION("معاینه واژینال"),
    PUP_SMEAR("Papsmear"),
    BIOPSY("Biopsy");

    String faTranslate;

    WomenServicePlus(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
