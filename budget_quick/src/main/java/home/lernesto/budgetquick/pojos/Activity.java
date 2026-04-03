package home.lernesto.budgetquick.pojos;


public class Activity {
    public static final int DEFAULT_ID = 0;

    private final int idActivity;
    private final String nameAct;
    private final String category;
    private final String um;
    private final int price;

//        Constructor
    public Activity(int idActivity, String nameAct, String category, String um, int price) {
        this.idActivity = idActivity;
        this.nameAct = nameAct;
        this.category = category;
        this.um = um;
        this.price = price;
    }

    //        GET
    public int getIdActivity() {
        return idActivity;
    }

    public String getNameAct() {
        return nameAct;
    }

    public String getCategory() {
        return category;
    }

    public String getUm() {
        return um;
    }

    public int getPrice() {
        return price;
    }
}
