package four.group.jahadi.Enums.Module;

public enum DoneOrNot {
    DONE("انجام شده"), NOT_DONE("انجام نشده");

    String faTranslate;

    DoneOrNot(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
