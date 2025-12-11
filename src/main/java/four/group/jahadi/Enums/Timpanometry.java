package four.group.jahadi.Enums;

public enum Timpanometry {
    An("An"), As("As"), Ad("Ad"),
    C1("C1"), C2("C2"), B("B"),
    CNT("CNT"), POSITIVE_PRESSURE("Positive pressure");

    String faTranslate;
    Timpanometry(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }

    }
