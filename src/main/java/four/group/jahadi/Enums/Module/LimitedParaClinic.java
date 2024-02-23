package four.group.jahadi.Enums.Module;

public enum LimitedParaClinic {
    ECG("نوار قلب"), BANDAGE("پانسمان"), STITCH("بخیه"), SOUNDING("سونداژ");

    String faTranslate;

    LimitedParaClinic(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
