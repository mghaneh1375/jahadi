package four.group.jahadi.Enums.Module;

public enum Mama {
    SUPER_EXAMINATION("معاینه برست"),
    VAGINAL_EXAMINATION("معاینه واژینال"),
    PUP_SMEAR("Papsmear");

    String faTranslate;

    Mama(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
