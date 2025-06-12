package four.group.jahadi.Enums.Module;

public enum ShouldGive {

    SHOULD_GIVE("تعلق میگیرد"), NOT_SHOULD_GIVE("تعلق نمیگیرد");

    String faTranslate;

    ShouldGive(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
