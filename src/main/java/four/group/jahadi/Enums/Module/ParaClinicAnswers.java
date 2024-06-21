package four.group.jahadi.Enums.Module;

public enum ParaClinicAnswers {

    DONE("انجام شده"), UNDONE("انجام نشده"), UNNECESSARY("غیرضروری");

    String faTranslate;

    ParaClinicAnswers(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
