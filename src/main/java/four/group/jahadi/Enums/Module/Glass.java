package four.group.jahadi.Enums.Module;

public enum Glass {

    GIVE("عینک تحویل داده شد"), SHOULD_GIVE_SUN_GLASS("تجویز عینک آفتابی");

    String faTranslate;

    Glass(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
