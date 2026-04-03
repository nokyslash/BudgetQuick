package home.lernesto.budgetquick.db;


import java.util.ArrayList;

public class ConstDB {
    public static final String DATABASE_NAME = "BudgetQuick";
    public static final int DATABASE_VERSION = 2;



//      Projects Table's Attributes
    static final String TABLE_PROJECTS            = "Projects";
    public static final String COLUMN_IDP         = "_id_p";
    public static final String COLUMN_TCOST       = "t_cost";
    public static final String COLUMN_COST_TYPE   = "cost_type";
    public static final String COLUMN_NAMEP       = "name_p";
    public static final String COLUMN_CLIENT_NAME = "client_name";
    public static final String COLUMN_ADDRESS     = "address";
    public static final String COLUMN_LOCK        = "lock";

//       Activities Table's Attributes
    static final String TABLE_ACTIVITIES    = "Activities";
    public static final String COLUMN_IDA   = "_id_a";
    public static final String COLUMN_NAMEA = "name_a";
    public static final String COLUMN_CAT   = "category";
    public static final String COLUMN_UNIT  = "unit";
    public static final String COLUMN_PRICE = "price";

//       Materials Table's Attributes
    static final String TABLE_MATERIALS      = "Materials";
    public static final String COLUMN_IDM    = "_id_m";
    public static final String COLUMN_NAMEM  = "name_m";
    public static final String COLUMN_FORMAT = "format";

//       Activitie's Cost Table's Attributes
    static final String TABLE_COST_ACT         = "Cost_Act";
    static final String COLUMN_IDPC            = "id_pc";
    static final String COLUMN_IDAC            = "id_ac";
    public static final String COLUMN_SIZE     = "size";
    public static final String COLUMN_ROOM     = "room";
    public static final String COLUMN_DUAL     = "dual";
    public static final String COLUMN_PROGRESS = "progress";

//       Material's Expense Table's Attributes
    static final String TABLE_EXPENSE_MAT    = "Expense_Mat";
    static final String COLUMN_IDAE          = "id_ae";
    static final String COLUMN_IDME          = "id_me";
    public static final String COLUMN_AMOUNT = "amount";

//       Notes Table's Attributes
    static final String TABLE_NOTES        = "Notes";
    static final String COLUMN_IDPN        = "id_pn";
    public static final String COLUMN_IDN  = "_id_n";
    static final String COLUMN_DATE        = "date";
    public static final String COLUMN_BODY = "body";

//        Telephone Table's Attributes
    static final String TABLE_TELEPHONE       = "Telephone";
    static final String COLUMN_IDPT           = "id_pt";
    public static final String COLUMN_TEL     = "tel";
    public static final String COLUMN_CONTACT = "contact";

//    Others FINALS
    static final String TOTAL_COST    = "total_cost";
    static final String TOTAL_EXPENSE = "total_expense";
    static final String DECIMAL_PROGRESS = "total_expense";

//    Check the correct tables
    public static final String CHECK_TABLES = "SELECT name FROM sqlite_master WHERE name!='sqlite_sequence' AND name!='android_metadata'";

    public static ArrayList<String> getTablesInUse(){
        ArrayList<String> tables = new ArrayList<>(7);
        tables.add(TABLE_PROJECTS);
        tables.add(TABLE_ACTIVITIES);
        tables.add(TABLE_MATERIALS);
        tables.add(TABLE_COST_ACT);
        tables.add(TABLE_EXPENSE_MAT);
        tables.add(TABLE_NOTES);
        tables.add(TABLE_TELEPHONE);
        return tables;
    }

//       Statement Create Projects Table
    static final String TABLE_PROJECTS_CREATE = "CREATE TABLE " + TABLE_PROJECTS + " (" +
            COLUMN_IDP         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TCOST       + " INTEGER NOT NULL, " +
            COLUMN_COST_TYPE   + " TEXT NOT NULL, " +
            COLUMN_NAMEP       + " TEXT NOT NULL, " +
            COLUMN_CLIENT_NAME + " TEXT NOT NULL, " +
            COLUMN_ADDRESS     + " TEXT NOT NULL, " +
            COLUMN_LOCK        + " BOOL NOT NULL)";

//       Statement Update Projects Table (column lock added)
    static final String TABLE_PROJECTS_UPDATE = "ALTER TABLE " + TABLE_PROJECTS +
            " ADD " + COLUMN_LOCK + " BOOL DEFAULT TRUE";

//       Statement Create Activites Table
    static final String TABLE_ACTIVITIES_CREATE = "CREATE TABLE " + TABLE_ACTIVITIES + " (" +
            COLUMN_IDA   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAMEA + " TEXT NOT NULL, " +
            COLUMN_CAT   + " TEXT NOT NULL, " +
            COLUMN_UNIT  + " TEXT NOT NULL, " +
            COLUMN_PRICE + " INT NOT NULL)";

//     Statement update Activities table (column price_cuc renamed to price)
    static final String TABLE_ACTIVITIES_RENAME_FOR_DELETE = "ALTER TABLE " + TABLE_ACTIVITIES + " RENAME TO Temp";
    static final String COPY_FROM_TEMP_TABLE_TO_NEW_ACTIVITIES_TABLE = "INSERT INTO " + TABLE_ACTIVITIES + "(" +
            COLUMN_IDA + "," + COLUMN_NAMEA + "," + COLUMN_CAT + "," + COLUMN_UNIT + "," + COLUMN_PRICE + ") SELECT " +
            COLUMN_IDA + "," + COLUMN_NAMEA + "," + COLUMN_CAT + "," + COLUMN_UNIT + ",price_cuc FROM Temp";
    static final String DELETE_OLD_TABLE_ACTIVITIES = "DROP TABLE Temp";

//       Statement Create Materials Table
    static final String TABLE_MATERIALS_CREATE = "CREATE TABLE " + TABLE_MATERIALS + " (" +
            COLUMN_IDM    + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAMEM  + " TEXT NOT NULL, " +
            COLUMN_FORMAT + " TEXT NOT NULL)";

    //   Statement Create Cost Table
    static final String TABLE_COST_ACT_CREATE = "CREATE TABLE " + TABLE_COST_ACT + " (" +
            COLUMN_IDPC     + " INTEGER NOT NULL, " +
            COLUMN_IDAC     + " INTEGER NOT NULL, " +
            COLUMN_SIZE     + " DOUBLE NOT NULL, " +
            COLUMN_ROOM     + " TEXT NOT NULL, " +
            COLUMN_DUAL     + " BOOL NOT NULL, " +
            COLUMN_PROGRESS + " DOUBLE NOT NULL DEFAULT 0)";

//       Statement Update Cost Table (column progress added, column status removed )
    static final String TABLE_COST_ACT_RENAME_FOR_DELETE = "ALTER TABLE " + TABLE_COST_ACT + " RENAME TO Temp";
    static final String COPY_FROM_TEMP_TABLE_TO_NEW_COST_ACT_TABLE = "INSERT INTO " + TABLE_COST_ACT + "(" +
            COLUMN_IDPC + "," + COLUMN_IDAC + "," + COLUMN_SIZE + "," + COLUMN_ROOM + "," + COLUMN_DUAL + ") SELECT " +
            COLUMN_IDPC + "," + COLUMN_IDAC + "," + COLUMN_SIZE + "," + COLUMN_ROOM + "," + COLUMN_DUAL + " FROM Temp";
    static final String DELETE_OLD_TABLE_COST_ACT = "DROP TABLE Temp";


//       Statement Create Expense Table
    static final String TABLE_EXPENSE_MAT_CREATE = "CREATE TABLE " + TABLE_EXPENSE_MAT + " (" +
            COLUMN_IDAE   + " INTEGER NOT NULL, " +
            COLUMN_IDME   + " INTEGER NOT NULL, " +
            COLUMN_AMOUNT + " DOUBLE NOT NULL)";
            /*"FOREIGN KEY (" + COLUMN_IDME + ") " +
            "REFERENCES " + TABLE_MATERIALS + "(" + COLUMN_IDM + "))";*/

//       Statement Create Notes Table
    static final String TABLE_NOTES_CREATE = "CREATE TABLE " + TABLE_NOTES + " (" +
            COLUMN_IDPN + " INTEGER NOT NULL, " +
            COLUMN_IDN  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DATE + " DATETIME NOT NULL, " +
            COLUMN_BODY + " TEXT NOT NULL)";

//       Statement Create Telephone Table
    static final String TABLE_TELEPHONE_CREATE = "CREATE TABLE " + TABLE_TELEPHONE + " (" +
            COLUMN_IDPT    + " INTEGER NOT NULL, " +
            COLUMN_TEL     + " TEXT NOT NULL, " +
            COLUMN_CONTACT + " TEXT NOT NULL)";

}
