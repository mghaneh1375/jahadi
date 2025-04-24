package four.group.jahadi.Enums;

public enum ExternalReferralTrackingStatus {
    NOT_COMP_DOC("نقص مدارک"),
    USER_NOT_TRACK("عدم پیگیری بیمار"),
    SURGERY("جراحی و بستری"),
    RAPID_SERV("خدمات سرپایی");

    private final String faTranslate;

    ExternalReferralTrackingStatus(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
