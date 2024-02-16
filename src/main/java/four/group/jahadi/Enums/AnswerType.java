package four.group.jahadi.Enums;

public enum AnswerType {

    TEXT, NUMBER, CHECKBOX, MULTI_SELECT, RADIO, TICK, DOUBLE, LONG_TEXT;

    public String getName() {
        return name().toLowerCase();
    }
}
