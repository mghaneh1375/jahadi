package four.group.jahadi.Enums.Module;

public enum DrugBackground {

    HAS_DRUG_BACKGROUND("سابقه مصرف دارو دارد"), HAS_NOT_DRUG_BACKGROUND("سابقه مصرف دارو ندارد");

    String faTranslate;

    DrugBackground(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
