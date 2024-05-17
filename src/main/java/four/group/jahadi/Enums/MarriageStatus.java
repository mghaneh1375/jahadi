package four.group.jahadi.Enums;

public enum MarriageStatus {
    MARRIED("متاهل"), SINGLE("مجرد"), DIVORCED("طلاق گرفته"), DEAD("همسر فوت شده");

    String faTranslate;

    MarriageStatus(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
