package home.lernesto.budgetquick.pojos;

public class CostActExtended {
    private final String nameAct;
    private final String um;
    private final CostAct costAct;

//    Constructor


    public CostActExtended(String nameAct, String um, CostAct costAct) {
        this.nameAct = nameAct;
        this.um = um;
        this.costAct = costAct;
    }

    //    GET
    public String getNameAct() {
        return nameAct;
    }

    public String getUm() {
        return um;
    }

    public CostAct getCostAct() {
        return costAct;
    }
}
