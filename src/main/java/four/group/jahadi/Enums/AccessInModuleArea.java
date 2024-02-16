package four.group.jahadi.Enums;

public enum AccessInModuleArea {

    FULL, RESPONSIBLE, SECRETARY, NONE;

    public String getName() {
        return name().toLowerCase();
    }
}
