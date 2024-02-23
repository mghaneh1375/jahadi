package four.group.jahadi.Enums.Module;

public enum AllParaClinic {

    ECG("نوار قلب"), BANDAGE("پانسمان"), STITCH("بخیه"), SOUNDING("سونداژ"),
    DRUG_IV("تزریق IV (دارو درمانی)"), IV("تزریق IV (سرم درمانی)"), IM("تزریق IM");

    String faTranslate;

    AllParaClinic(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
