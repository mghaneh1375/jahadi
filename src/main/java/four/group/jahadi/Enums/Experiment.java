package four.group.jahadi.Enums;

public enum Experiment {
    CBC("CBC"), UA("UA"),
    RAPID_TEST_BETA_HCG("rapid test beta HCG"),
    FBS("(بیمار برای این آزمایش باید ناشتا باشد)FBS"),
    UREA("Urea(BUN)"),
    CREATININE("creatinine"),
    CHOLESTEROL("cholesterol(بیمار برای این آزمایش باید ناشتا باشد)"),
    TRIGLYCERIDE("Triglyceride(بیمار برای این آزمایش باید ناشتا باشد)"),
    HDL("(بیمار برای این آزمایش باید ناشتا باشد)HDL"),
    LDL("(بیمار برای این آزمایش باید ناشتا باشد)LDL"),
    AST("AST(SGOT)"), ALT("ALT(SGPT)"),
    TSH("TSH"), LH("LH"), FSH("FSH");

    private final String faTranslate;

    Experiment(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return this.faTranslate;
    }
}
