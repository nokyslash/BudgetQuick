package home.lernesto.budgetquick.pojos;


public class Material {
    public static final int DEFAULT_ID = 0;

    private final int idMaterial;
    private final String nameMaterial;
    private final String format;

//        Constructor


    public Material(int idMaterial, String nameMaterial, String format) {
        this.idMaterial = idMaterial;
        this.nameMaterial = nameMaterial;
        this.format = format;
    }

    //        GET
    public int getIdMaterial() {
        return idMaterial;
    }

    public String getNameMaterial() {
        return nameMaterial;
    }

    public String getFormat() {
        return format;
    }
}
