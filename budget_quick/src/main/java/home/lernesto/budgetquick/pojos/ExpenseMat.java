package home.lernesto.budgetquick.pojos;

public class ExpenseMat {
    private final int idAct;
    private final int idMat;
    private final double amount;

//    Constructor
    public ExpenseMat(int idAct, int idMat, double amount) {
        this.idAct = idAct;
        this.idMat = idMat;
        this.amount = amount;
    }


//    GET
    public int getIdAct() {
        return idAct;
    }

    public int getIdMat() {
        return idMat;
    }

    public double getAmount() {
        return amount;
    }
}
