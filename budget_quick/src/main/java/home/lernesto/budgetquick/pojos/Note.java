package home.lernesto.budgetquick.pojos;


import java.util.Date;

public class Note {
    public static final int DEFAULT_ID = 0;

    private final int idP;
    private final int idN;
    private final Date date;
    private final String body;

    public Note(int idP, int idN, Date date, String body) {
        this.idP = idP;
        this.idN = idN;
        this.date = date;
        this.body = body;
    }

    public int getIdP() {
        return idP;
    }

    public int getIdN() {
        return idN;
    }

    public Date getDate() {
        return date;
    }

    public String getBody() {
        return body;
    }
}
