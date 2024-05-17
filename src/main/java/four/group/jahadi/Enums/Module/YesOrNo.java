package four.group.jahadi.Enums.Module;

public enum YesOrNo {
    YES("بله"), NO("خیر");

    String faTranslate;

    YesOrNo(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
