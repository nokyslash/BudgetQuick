package home.lernesto.budgetquick.db;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import home.lernesto.budgetquick.pojos.ExpenseMat;
import home.lernesto.budgetquick.pojos.ExpenseMatExtended;
//import home.lernesto.budgetquick.pojos.Material;

public class DataExpenseMat {
    public static ArrayList<ExpenseMatExtended> getData(Context context, int idAct){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.getExpenseMat(idAct);
    }

    public static String[] calcExpenseTotal(Context context, int idPro){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.calculateMaterialExpenseTotal(idPro);
    }

    public static  String[] calcExpenseByRoom(Context context, int idPro, String room){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.calculateMaterialExpenseByRoom(idPro, room);
    }

    public static String[] calcExpenseByAct(Context context, int idPro, int idAct){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.calculateMaterialExpenseByAct(idPro, idAct);
    }

    public static void insert(Context context, ExpenseMat expenseMat){
        ContentValues values = new ContentValues();
        values.put(ConstDB.COLUMN_IDAE, expenseMat.getIdAct());
        values.put(ConstDB.COLUMN_IDME, expenseMat.getIdMat());
        values.put(ConstDB.COLUMN_AMOUNT, expenseMat.getAmount());

        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.insertExpenseMaterial(values);
    }

    public static void edit(Context context, ExpenseMat expenseMat,int oldIdMat){
        ContentValues editedValues = new ContentValues();
        editedValues.put(ConstDB.COLUMN_IDME, expenseMat.getIdMat());
        editedValues.put(ConstDB.COLUMN_AMOUNT, expenseMat.getAmount());

        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.editExpenseMaterial(editedValues, expenseMat.getIdAct(), oldIdMat);
    }

    public static void deleteDirect(Context context, int idAct,int idMat){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.deleteDirectExpenseMaterial(idAct,idMat);
    }

    static void deleteIndirectMat(Context context, int idMat){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.deleteIndirectExpenseMaterialByMat(idMat);
    }

    static void deleteIndirectAct(Context context, int idAct){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.deleteIndirectExpenseMaterialbyAct(idAct);
    }

    public static boolean existElement(Context context,int idAct, int idMat){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.existElementInExpenseMat(idAct, idMat);
    }
}
