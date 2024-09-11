package four.group.jahadi.Enums;

public enum EquipmentHealthStatus {
    VALID, DAMAGED, FIXING;

    public String getName() {
        return this.name().toLowerCase();
    }
}
