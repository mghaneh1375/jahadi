package four.group.jahadi.Enums.Module;

public enum PostDoctorOptions {

    TUTORIAL("آموزش"), MAG("بروشور");

    String faTranslate;

    PostDoctorOptions(String faTranslate) {
        this.faTranslate = faTranslate;
    }

    public String getFaTranslate() {
        return faTranslate;
    }

}
