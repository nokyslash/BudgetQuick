package home.lernesto.budgetquick.db;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import home.lernesto.budgetquick.pojos.ClientsPro;
import home.lernesto.budgetquick.pojos.Project;

public class DataProjects {

    public static ArrayList<Project> getData(Context context){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.getProjects();
    }

    public static ClientsPro getDataClient(Context context, int idPro){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.getDataClient(idPro);
    }

    public static int getTCost(Context context, int idPro){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.getTotalCostByIdPro(idPro);
    }

    public static void lockUnlock(Context context,Project project, ClientsPro clientsPro) {
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        project.setLock(!db.isLockProject(project.getIdProject()));
        edit(context, project, clientsPro);
    }

    public static void insert(Context context, Project project, ClientsPro clientsPro){
        ContentValues newValues = new ContentValues();
        newValues.put(ConstDB.COLUMN_TCOST, project.getTotalCost());
        newValues.put(ConstDB.COLUMN_COST_TYPE, project.getCostType());
        newValues.put(ConstDB.COLUMN_NAMEP,project.getNameProject());
        newValues.put(ConstDB.COLUMN_LOCK,project.isLock());
        newValues.put(ConstDB.COLUMN_CLIENT_NAME,clientsPro.getName());
        newValues.put(ConstDB.COLUMN_CLIENT_NAME,clientsPro.getName());
        newValues.put(ConstDB.COLUMN_ADDRESS,clientsPro.getClientAddress());

        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.insertProject(newValues);
    }

    public static void edit(Context context, Project project, ClientsPro clientsPro){
        ContentValues editedValues = new ContentValues();
        editedValues.put(ConstDB.COLUMN_TCOST, project.getTotalCost());
        editedValues.put(ConstDB.COLUMN_COST_TYPE, project.getCostType());
        editedValues.put(ConstDB.COLUMN_NAMEP,project.getNameProject());
        editedValues.put(ConstDB.COLUMN_CLIENT_NAME,clientsPro.getName());
        editedValues.put(ConstDB.COLUMN_ADDRESS,clientsPro.getClientAddress());
        editedValues.put(ConstDB.COLUMN_LOCK,project.isLock());

        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.editProject(editedValues, project.getIdProject());
    }

    public static void editTotalCost(Context context, int idPro, int newTotal){
        ContentValues values = new ContentValues();
        values.put(ConstDB.COLUMN_TCOST, newTotal);

        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.editProject(values, idPro);
    }

    public static void delete(Context context, int id){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.deleteProject(id);
        DataCostAct.deleteIndirectPro(context, id);
        DataNotes.indirectDelete(context, id);
        DataContact.indirectDelete(context, id);
    }

    public static boolean isFinished(Context context, int idPro){
        return DataCostAct.countAct(context, idPro) == DataCostAct.countFinished(context, idPro);
    }
}
