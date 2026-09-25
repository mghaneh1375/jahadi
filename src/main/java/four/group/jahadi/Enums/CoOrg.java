package four.group.jahadi.Enums;

import lombok.Getter;

public enum CoOrg {

    SEPAH("سپاه پاسداران"), IMAM_COM("کمیته امداد"),
    ALAVI("بنیاد علوی"), BEH("بهزیستی"),
    HELAL("حلال احمر"), CIVIL("شهرداری تهران"),
    GOV("وزارت کشور"), BON("بنیاد مستضعفان"),
    IMAM("ستاد اجرایی فرمان امام"), BASIJ("سازمان بسیج"),
    EMPTY("بدون نهاد");

    public String getName() { return name().toLowerCase(); }
    @Getter
    private final String faTranslate;
    CoOrg(String faTranslate) {
        this.faTranslate = faTranslate;
    }
}
