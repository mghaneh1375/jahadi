package four.group.jahadi.Enums.Module;

public enum AnswerType {

    TEXT, NUMBER, CHECKBOX, MULTI_SELECT, SELECT,
    RADIO, TICK, DOUBLE, LONG_TEXT, UPLOAD;

    public String getName() {
        return name().toLowerCase();
    }
}
