package home.lernesto.budgetquick.db;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import home.lernesto.budgetquick.pojos.Activity;

public class DataActivity {
    public static ArrayList<Activity> getActivities(Context context, String category){
        DataBaseHelperClass db = new DataBaseHelperClass(context);

        if (category.equals("Total"))return db.getActivities();
        else return db.getActivitiesByCategories(category);
    }

    public static Activity findAct(Context context, int idAct){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.findActivity(idAct);
    }

    public static void insert (Context context, Activity activity){
        ContentValues newValues = new ContentValues();
        newValues.put(ConstDB.COLUMN_NAMEA, activity.getNameAct());
        newValues.put(ConstDB.COLUMN_CAT, activity.getCategory());
        newValues.put(ConstDB.COLUMN_UNIT, activity.getUm());
        newValues.put(ConstDB.COLUMN_PRICE, activity.getPrice());

        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.insertActivity(newValues);
    }

    public static void edit(Context context, Activity activity){
        ContentValues editedValues = new ContentValues();
        editedValues.put(ConstDB.COLUMN_NAMEA, activity.getNameAct());
        editedValues.put(ConstDB.COLUMN_CAT, activity.getCategory());
        editedValues.put(ConstDB.COLUMN_UNIT, activity.getUm());
        editedValues.put(ConstDB.COLUMN_PRICE, activity.getPrice());

        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.editActivity(editedValues, activity.getIdActivity());
    }

    public static void delete(Context context, int id){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.deleteActivity(id);
        DataExpenseMat.deleteIndirectAct(context, id);
        DataCostAct.deleteIndirectAct(context, id);
    }
}
