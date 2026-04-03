package home.lernesto.budgetquick.pojos;


public class Project {
    public static final int TEMP_ID = -1;
    public static final int INITIAL_COST = 0;
    public static final String FIX_COST = "fix";
    public static final String VARIABLE_COST = "var";
    public static final boolean DEFAULT_LOCK = false;

    private final int idProject;
    private final int totalCost;
    private final String costType;
    private final String nameProject;
    private boolean lock;

//    Constructor
    public Project(int idProject, int totalCost, String costType, String nameProject, boolean lock) {
        this.idProject = idProject;
        this.totalCost = totalCost;
        this.costType = costType;
        this.nameProject = nameProject;
        this.lock = lock;
    }


//    GET
    public int getIdProject() {
        return idProject;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public String getCostType() {
        return costType;
    }

    public String getNameProject() {
        return nameProject;
    }

    public boolean isLock(){return lock;}

    public void setLock(boolean lock){this.lock = lock;}
}
