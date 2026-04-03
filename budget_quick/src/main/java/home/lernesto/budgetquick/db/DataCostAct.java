package home.lernesto.budgetquick.db;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import home.lernesto.budgetquick.pojos.CostAct;
import home.lernesto.budgetquick.pojos.CostActExtended;

public class DataCostAct {

    public static ArrayList<CostActExtended> getDataByRoom(Context context, int idPro, String room){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.getCostActivitiesByRoom(idPro, room);
    }

    public static ArrayList<CostActExtended> getDataByProject(Context context, int idPro){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.getCostActivitesByProjects(idPro);
    }

    public static ArrayList<CostActExtended> getDataDualByProject(Context context, int idPro){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.getCostActDualByProjects(idPro);
    }

    public static String[] getRooms(Context context, int idPro){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        ArrayList<String> lRooms = db.getRoomsOfPro(idPro);
        String[] rooms = new String[lRooms.size()];

       for (int i = 0; i < lRooms.size(); i++){
           rooms[i] = lRooms.get(i);
       }

        return rooms;
    }

    public static String[] getUses(Context context, int idAct){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        ArrayList<String> names = db.getUsesProject(idAct);
        String[] result = new String[names.size()];

        for (int i = 0; i < names.size(); i++) result[i] = names.get(i);

        return result;
    }

    public static boolean isUsed(Context context, int idAct){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return !db.getUsesProject(idAct).isEmpty();
    }

    public static boolean containsExpenseMat(Context context, int idAct){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return !db.getExpenseMat(idAct).isEmpty();
    }

    public static int countAct(Context context, int idPro){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.countCostActByPro(idPro);
    }

    public static int countFinished(Context context, int idPro){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.countActivitiesFinished(idPro);
    }

    public static int calcTCost(Context context, int idPro){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.calculateTotalCost(idPro);
    }

    public static int calcTCostByRoom(Context context, int idPro, String room){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.calculateTotalCostByRoom(idPro, room);
    }

    public static int calcTCostByAct(Context context, int idPro, int idAct){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.calculateTotalCostByAct(idPro, idAct);
    }

    public static double getSize(Context context, int idPro, int idAct, String room){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.getSingleSize(idPro, idAct, room);
    }

    public static void insert(Context context, CostAct costAct){
        ContentValues values = new ContentValues();
        values.put(ConstDB.COLUMN_IDPC, costAct.getIdPro());
        values.put(ConstDB.COLUMN_IDAC, costAct.getIdAct());
        values.put(ConstDB.COLUMN_SIZE, costAct.getSize());
        values.put(ConstDB.COLUMN_ROOM, costAct.getRoom());
        values.put(ConstDB.COLUMN_DUAL, costAct.isDual());
//        values.put(ConstDB.COLUMN_STATUS_A, false);

        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.insertCostActivity(values);
    }

    public static void edit(Context context, CostAct costAct, int oldIdAct){
        ContentValues values = new ContentValues();
        values.put(ConstDB.COLUMN_SIZE, costAct.getSize());
        values.put(ConstDB.COLUMN_ROOM, costAct.getRoom());
        values.put(ConstDB.COLUMN_DUAL, costAct.isDual());
        values.put(ConstDB.COLUMN_PROGRESS, costAct.getProgress());

        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.editCostActivity(values, costAct.getIdPro(), oldIdAct);
    }

    public static void updateNewProgress(Context context, CostAct costAct){
        ContentValues values = new ContentValues();
        values.put(ConstDB.COLUMN_PROGRESS, costAct.getProgress());

        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.editCostActivity(values, costAct.getIdPro(), costAct.getIdAct());
    }

    public static void toDual(Context context, int idPro){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        ArrayList<Integer> idsAct = db.getIdsAssignedAct(idPro);

        for (Integer i : idsAct){
            ContentValues values = new ContentValues();
            values.put(ConstDB.COLUMN_DUAL,true);

            db.editCostActivity(values,idPro, i);
        }
    }

    public static void toNotDual(Context context, int idPro){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        ArrayList<Integer> idsAct = db.getIdsAssignedAct(idPro);

        for (Integer i : idsAct){
            ContentValues values = new ContentValues();
            values.put(ConstDB.COLUMN_DUAL,false);

            db.editCostActivity(values,idPro, i);
        }
    }

    public static void plusSize(Context context, int idPro, int idAct, double plusSize, String room){
        DataBaseHelperClass db = new DataBaseHelperClass(context);

        ContentValues values = new ContentValues();
        values.put(ConstDB.COLUMN_SIZE, db.getSingleSize(idPro, idAct, room) + plusSize);
        db.editCostActivityByRoom(values, idPro, idAct, room);

    }

    public static void overWriteSize (Context context, int idPro, int idAct, double newSize, String room){
        ContentValues values = new ContentValues();
        values.put(ConstDB.COLUMN_SIZE, newSize);

        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.editCostActivityByRoom(values, idPro, idAct, room);
    }

    public static void deleteDirect(Context context,int idPro, int idAct, String room){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.deleteDirectCostActivity(idPro, idAct, room);
    }

    public static int getProgressProInPercent(Context context, int idPro){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return (int)(db.getProgressOfProject(idPro)*100);
    }

    public static int getProgressActInPercent(Context context, int idPro, int idAct){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return (int)(db.getProgressOfActivity(idPro, idAct)*100);
    }

    static void deleteIndirectPro(Context context, int idPro){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.deleteIndirectCostActivitybyPro(idPro);
    }

    static void deleteIndirectAct(Context context, int idAct){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.deleteIndirectCostActivitybyAct(idAct);
    }
}
