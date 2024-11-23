package four.group.jahadi.Enums;

public enum Sex {

    MALE("آقا"), FEMALE("خانم");
    private final String faTranslate;
    Sex(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }

    public String getName() {
        return name().toLowerCase();
    }

}
