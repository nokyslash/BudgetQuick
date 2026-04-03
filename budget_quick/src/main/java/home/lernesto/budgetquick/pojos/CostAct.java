package home.lernesto.budgetquick.pojos;

public class CostAct {
    public static final double DEFAULT_PROGRESS = 0.0;

    private final int idPro;
    private final int idAct;
    private final double size;
    private final String room;
    private final boolean dual;
    private double progress;


//    Constructor
    public CostAct(int idPro, int idAct, double size, String room, boolean dual, double progress) {
        this.idPro    = idPro;
        this.idAct    = idAct;
        this.size     = size;
        this.room     = room;
        this.dual     = dual;
        this.progress = progress;
    }

    //    GET
    public int getIdPro() {
        return idPro;
    }

    public int getIdAct() {
        return idAct;
    }

    public double getSize() { return size; }

    public String getRoom() {
        return room;
    }

    public boolean isDual() {
        return dual;
    }

    public double getProgress(){return progress;}

    public void setProgress(double progress){this.progress = progress;}
}
