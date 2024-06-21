package four.group.jahadi.Enums.Module;

public enum RavanAnswers {

    NEVER("هرگز", 0), SOME_TIME("گاهی اوقات", 1),
    OFTEN("بیشتر اوقات", 2), ALWAYS("همواره", 3);

    String faTranslate;
    int point;

    RavanAnswers(String faTranslate, int point) {
        this.faTranslate = faTranslate;
        this.point = point;
    }

    public String getFaTranslate() {
        return faTranslate;
    }

    public int getPoint() {
        return point;
    }
}
