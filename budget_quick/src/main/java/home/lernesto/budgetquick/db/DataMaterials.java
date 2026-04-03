package home.lernesto.budgetquick.db;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import home.lernesto.budgetquick.pojos.Material;

public class DataMaterials {
    public static ArrayList<Material> getData(Context context){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.getMaterials();
    }

    public static void insert(Context context, Material material){
        ContentValues newValues = new ContentValues();
        newValues.put(ConstDB.COLUMN_NAMEM, material.getNameMaterial());
        newValues.put(ConstDB.COLUMN_FORMAT, material.getFormat());

        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.insertMaterial(newValues);
    }

    public static void edit(Context context, Material material){
        ContentValues editedValues = new ContentValues();
        editedValues.put(ConstDB.COLUMN_NAMEM,material.getNameMaterial());
        editedValues.put(ConstDB.COLUMN_FORMAT,material.getFormat());

        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.editMaterial(editedValues, material.getIdMaterial());
    }

    public static void delete(Context context, int id){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.deleteMaterial(id);
        DataExpenseMat.deleteIndirectMat(context, id);
    }
}
