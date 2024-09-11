package four.group.jahadi.Enums.Drug;

public enum DrugType {
    TAB, CAP, PEARL, OINT, CREAM, GEL, SUS,
    SYR, SUPP, AMP, WATER, SERUM, DROP, SPRAY,
    EMULSION, LOTION, POWDER, SHAMPOO, EFF_TAB,
    SOAP;

    public String getName() {
        return name().toLowerCase();
    }
}
