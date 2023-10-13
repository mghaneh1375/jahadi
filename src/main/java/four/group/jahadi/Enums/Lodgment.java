package four.group.jahadi.Enums;

public enum Lodgment {

    OFFICE, MOSQUE, UNIVERSITY, BASIJ, OTHER;

    public String getName() {
        return name().toLowerCase();
    }

}
