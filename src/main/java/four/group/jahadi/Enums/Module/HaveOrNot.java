package four.group.jahadi.Enums.Module;

public enum HaveOrNot {
    HAVE("دارد"), NOT_HAVE("ندارد");

    String faTranslate;

    HaveOrNot(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
