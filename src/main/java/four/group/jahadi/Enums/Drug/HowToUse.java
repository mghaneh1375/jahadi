package four.group.jahadi.Enums.Drug;

public enum HowToUse {

    FULL("با معده پر (بعد غذا)"), HUNGRY("با معده خالی (قبل غذا)"),
    WITH_FOOD("همراه غذا"), FASTING("ناشتا"),
    SUBLINGUAL("زیر زبونی"), MORNING("صبح ها"),
    AFTERNOON("عصرها"), NIGHTS("شب ها"),
    BEFORE_LAUNCH_AND_DINNER("قبل از ناهار و شام"),
    AFTER_LAUNCH_AND_DINNER("بعد از ناهار و شام"),
    VAGINAL_USE("استعمال واژینال"),
    ANAL_USE("استعمال مقعدی"), SPRAY("اسپری شود"),
    IF_NEEDED("در صورت نیاز"), ACCORDING_TO_MANUAL("طبق دستور"),
    VEIN("وریدی (IV)"), MUSCULAR("عضلانی (IM)"),
    SERUM("داخل سرم");

    String faTranslate;

    HowToUse(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }
}
