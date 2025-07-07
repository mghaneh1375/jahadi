package four.group.jahadi.Enums.Drug;

import lombok.Getter;

public enum DrugLocation {
    MAIN("اصلی"), FUNCTIONAL("کاربری"), LARGE("اصلی"), SMALL("فرعی");

    @Getter
    String faTranslate;
    DrugLocation(String faTranslate) { this.faTranslate = faTranslate; }

    public String getName() {
        return name().toLowerCase();
    }
}
