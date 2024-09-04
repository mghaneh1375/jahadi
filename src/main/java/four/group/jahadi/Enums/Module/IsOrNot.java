package four.group.jahadi.Enums.Module;

public enum IsOrNot {
    IS("است"), NOT("نیست");

    String faTranslate;

    IsOrNot(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
