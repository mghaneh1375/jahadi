package four.group.jahadi.Enums.Module;

public enum DiseaseBackground {

    HAS_DISEASE_BACKGROUND("سابقه بیماری دارد"), HAS_NOT_DISEASE_BACKGROUND("سابقه بیماری ندارد");

    String faTranslate;

    DiseaseBackground(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
