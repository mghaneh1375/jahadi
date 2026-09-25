package four.group.jahadi.Enums;

import lombok.Getter;

public enum BloodType {

    A_PLUS("A+"), A_MINUS("A-"),
    B_PLUS("B+"), B_MINUS("B-"),
    AB_PLUS("AB+"), AB_MINUS("AB-"),
    O_PLUS("O+"), O_MINUS("O-");

    public String getName() { return name().toLowerCase(); }
    @Getter
    private final String faTranslate;
    BloodType(String faTranslate) {
        this.faTranslate = faTranslate;
    }
}
