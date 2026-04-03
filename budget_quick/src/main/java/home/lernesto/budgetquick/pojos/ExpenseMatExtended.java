package home.lernesto.budgetquick.pojos;

public class ExpenseMatExtended {
    private final String nameMat;
    private final String format;
    private final ExpenseMat expenseMat;


//    Constructor
    public ExpenseMatExtended(String nameMat, String format, ExpenseMat expenseMat) {
        this.nameMat = nameMat;
        this.format = format;
        this.expenseMat = expenseMat;
    }


//    GET
    public String getNameMat() {
        return nameMat;
    }

    public String getFormat() {
        return format;
    }

    public ExpenseMat getExpenseMat() {
        return expenseMat;
    }
}
