package four.group.jahadi.Enums.Module;

public enum DeliveryStatus {

    DELIVERED("تحویل گرفته"), NOT_DELIVERED("تحویل نگرفته");

    String faTranslate;

    DeliveryStatus(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
