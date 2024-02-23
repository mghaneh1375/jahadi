package four.group.jahadi.Enums.Module;

public enum Audiologists {

    TIME("مدت زمان کم شنوایی"),
    VERTIGO("سرگیجه"),
    VZVZ("وزوز"),
    INJURY("ضربه به گوش"),
    FAMILY_HIST("سابقه کم شنوایی در خانواده"),
    FAMILY_MAR("ازدواج فامیلی والدین"),
    HOSPITAL_HIST("سابقه بستری در بیمارستان"),
    EXUDE("ترشح از گوش"),
    INFECT_HIST("سابقه عفونت مکرر");

    String faTranslate;

    Audiologists(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }

}
