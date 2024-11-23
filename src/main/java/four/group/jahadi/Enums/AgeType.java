package four.group.jahadi.Enums;

public enum AgeType {

    ADULT("بزرگسال"), CHILD("کودک");

    private final String faTranslate;
    AgeType(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
    public String getName() {
        return name().toLowerCase();
    }
}
