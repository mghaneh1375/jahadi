package four.group.jahadi.Enums;

public enum AccountStatus {

    ACTIVE, PENDING, BLOCKED;

    public String getName() {
        return name().toLowerCase();
    }

}
