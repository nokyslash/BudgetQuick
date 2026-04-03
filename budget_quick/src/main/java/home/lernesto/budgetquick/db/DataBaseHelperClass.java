package home.lernesto.budgetquick.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import home.lernesto.budgetquick.pojos.Activity;
import home.lernesto.budgetquick.pojos.ClientsPro;
import home.lernesto.budgetquick.pojos.Contact;
import home.lernesto.budgetquick.pojos.CostAct;
import home.lernesto.budgetquick.pojos.CostActExtended;
import home.lernesto.budgetquick.pojos.ExpenseMat;
import home.lernesto.budgetquick.pojos.ExpenseMatExtended;
import home.lernesto.budgetquick.pojos.Material;
import home.lernesto.budgetquick.pojos.Note;
import home.lernesto.budgetquick.pojos.Project;


public class DataBaseHelperClass extends SQLiteOpenHelper {

//    Create/Connect Data Base
    DataBaseHelperClass(Context context) {
        super(context, ConstDB.DATABASE_NAME, null, ConstDB.DATABASE_VERSION);
    }

//    Create Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ConstDB.TABLE_PROJECTS_CREATE);
        db.execSQL(ConstDB.TABLE_ACTIVITIES_CREATE);
        db.execSQL(ConstDB.TABLE_MATERIALS_CREATE);
        db.execSQL(ConstDB.TABLE_EXPENSE_MAT_CREATE);
        db.execSQL(ConstDB.TABLE_COST_ACT_CREATE);
        db.execSQL(ConstDB.TABLE_NOTES_CREATE);
        db.execSQL(ConstDB.TABLE_TELEPHONE_CREATE);
    }

    //    Update Tables
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 2){
            db.execSQL(ConstDB.TABLE_PROJECTS_UPDATE);
            db.execSQL(ConstDB.TABLE_ACTIVITIES_RENAME_FOR_DELETE);
            db.execSQL(ConstDB.TABLE_ACTIVITIES_CREATE);
            db.execSQL(ConstDB.COPY_FROM_TEMP_TABLE_TO_NEW_ACTIVITIES_TABLE);
            db.execSQL(ConstDB.DELETE_OLD_TABLE_ACTIVITIES);
            db.execSQL(ConstDB.TABLE_COST_ACT_RENAME_FOR_DELETE);
            db.execSQL(ConstDB.TABLE_COST_ACT_CREATE);
            db.execSQL(ConstDB.COPY_FROM_TEMP_TABLE_TO_NEW_COST_ACT_TABLE);
            db.execSQL(ConstDB.DELETE_OLD_TABLE_COST_ACT);
        }
    }



//    Project's Methods
    ArrayList<Project> getProjects(){
        ArrayList<Project> projects = new ArrayList<>();
        String[] col = new String[]{ConstDB.COLUMN_IDP, ConstDB.COLUMN_TCOST, ConstDB.COLUMN_COST_TYPE, ConstDB.COLUMN_NAMEP, ConstDB.COLUMN_LOCK};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                ConstDB.TABLE_PROJECTS,col,
                null,null,null,null,
                ConstDB.COLUMN_NAMEP + " ASC");

        if (cursor.moveToFirst()){
            do{
                projects.add(new Project(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getString(3), cursor.getInt(4) > 0));
            }while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return projects;
    }

    ClientsPro getDataClient(int idPro){
        ClientsPro clientsPro = null;
        String[] col = new String[]{ConstDB.COLUMN_CLIENT_NAME, ConstDB.COLUMN_ADDRESS};
        String[] arg =  new String[]{Integer.toString(idPro)};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                ConstDB.TABLE_PROJECTS, col,
                ConstDB.COLUMN_IDP + "=?", arg,
                null, null, null);

        if (cursor.moveToFirst()){
            clientsPro = new ClientsPro(cursor.getString(0), cursor.getString(1));
        }
        cursor.close();
        db.close();

        return clientsPro;
    }

    int getTotalCostByIdPro(int idPro){
        int result = -1;

        SQLiteDatabase db = this.getReadableDatabase();
        String[] col = new String[]{ConstDB.COLUMN_TCOST};
        String[] arg = new String[]{Integer.toString(idPro)};

        Cursor cursor = db.query(
                ConstDB.TABLE_PROJECTS, col,
                ConstDB.COLUMN_IDP + "=?", arg,
                null, null, null);

        if (cursor.moveToFirst()) result = cursor.getInt(0);

        cursor.close();
        db.close();
        return result;
    }

    boolean isLockProject(int idPro){
        boolean result = false;
        String[] col = new String[]{ConstDB.COLUMN_LOCK};
        String[] arg = new String[]{Integer.toString(idPro)};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                ConstDB.TABLE_PROJECTS, col, ConstDB.COLUMN_IDP + "=?", arg,
                null,null,null);

        if (cursor.moveToFirst()) result = cursor.getInt(0) != 0;
        cursor.close();
        db.close();

        return result;
    }

    void insertProject(ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ConstDB.TABLE_PROJECTS, null, values);
        db.close();
    }

    void editProject(ContentValues editedValues, int id){
        String[] arg = new String[]{Integer.toString(id)};

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(ConstDB.TABLE_PROJECTS, editedValues, ConstDB.COLUMN_IDP + "=?", arg);
        db.close();
    }

    void deleteProject(int id){
        String[] arg = new String[]{Integer.toString(id)};

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ConstDB.TABLE_PROJECTS, ConstDB.COLUMN_IDP + "=?", arg);
        db.close();
    }


//    Activitie's Methods
    ArrayList<Activity> getActivities(){
        ArrayList<Activity> activities = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                ConstDB.TABLE_ACTIVITIES,null, null,null,null,null, ConstDB.COLUMN_NAMEA + " ASC");

        if (cursor.moveToFirst()){
            do {
                activities.add(
                        new Activity(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4)));
            }while (cursor.moveToNext());

            cursor.close();
        }
        db.close();
        return activities;
    }

    ArrayList<Activity> getActivitiesByCategories(String cat){
        ArrayList<Activity> activities = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] arg = {cat};
        Cursor cursor = db.query(
                ConstDB.TABLE_ACTIVITIES, null,
                ConstDB.COLUMN_CAT + "=?", arg,
                null, null,
                ConstDB.COLUMN_NAMEA + " ASC");

        if (cursor.moveToFirst()){
            do {
                activities.add(
                        new Activity(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4)));

            }while (cursor.moveToNext());

            cursor.close();
        }
        db.close();
        return activities;
    }

    Activity findActivity(int idAct){
        Activity result = null;
        String[] arg = new String[]{Integer.toString(idAct)};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                ConstDB.TABLE_ACTIVITIES, null,
                ConstDB.COLUMN_IDA + "=?", arg,
                null, null, null
        );

        if (cursor.moveToFirst()){
            result = new Activity(idAct, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
            cursor.close();
        }

        db.close();
        return result;
    }

    void insertActivity(ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ConstDB.TABLE_ACTIVITIES, null, values);
        db.close();
    }

    void editActivity(ContentValues editedValues, int id){
        String[] arg = new String[]{Integer.toString(id)};

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(ConstDB.TABLE_ACTIVITIES, editedValues, ConstDB.COLUMN_IDA + "=?", arg);
        db.close();
    }

    void deleteActivity(int id){
        String[] arg = new String[]{Integer.toString(id)};

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ConstDB.TABLE_ACTIVITIES, ConstDB.COLUMN_IDA + "=?", arg);
        db.close();
    }


//   Material's Methods
    ArrayList<Material> getMaterials(){
        ArrayList<Material> materials = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ConstDB.TABLE_MATERIALS, null, null, null, null, null, ConstDB.COLUMN_NAMEM + " ASC");

        if (cursor.moveToFirst()){
            do {
                materials.add(
                        new Material(
                                cursor.getInt(0),cursor.getString(1),cursor.getString(2)));
            }while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return materials;
    }

    void insertMaterial(ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ConstDB.TABLE_MATERIALS, null, values);
        db.close();
    }

    void editMaterial(ContentValues editedValues, int id){
        String[] arg = new String[]{Integer.toString(id)};

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(ConstDB.TABLE_MATERIALS, editedValues, ConstDB.COLUMN_IDM + "=?", arg);
        db.close();
    }

    void deleteMaterial(int id){
        String[] arg = new String[]{Integer.toString(id)};

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ConstDB.TABLE_MATERIALS, ConstDB.COLUMN_IDM + "=?", arg);
        db.close();
    }


//    Expense's Mathods
    ArrayList<ExpenseMatExtended> getExpenseMat(int idAct){
        ArrayList<ExpenseMatExtended> expenseMatExtendeds = new ArrayList<>();
        String[] arg = {Integer.toString(idAct)};

        final String query =
                "SELECT "
                        + ConstDB.COLUMN_IDME + ","
                        + ConstDB.COLUMN_AMOUNT + ","
                        + ConstDB.COLUMN_NAMEM + ","
                        + ConstDB.COLUMN_FORMAT +

                " FROM "
                        + ConstDB.TABLE_EXPENSE_MAT +
                " INNER JOIN "
                        + ConstDB.TABLE_MATERIALS +
                " ON "
                        + ConstDB.COLUMN_IDME + "=" + ConstDB.COLUMN_IDM +

                " WHERE "
                        + ConstDB.COLUMN_IDAE + "=?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,arg);

        if(cursor.moveToFirst()){
            do{
                expenseMatExtendeds.add(
                        new ExpenseMatExtended(
                                cursor.getString(2),
                                cursor.getString(3),
                                new ExpenseMat(
                                        idAct,
                                        cursor.getInt(0),
                                        cursor.getDouble(1))));
            }while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return expenseMatExtendeds;
    }

    String[] calculateMaterialExpenseTotal(int idPro){
        String[] result = null;
        String[] arg = new String[]{Integer.toString(idPro)};

        String query =
                "SELECT SUM(" + ConstDB.COLUMN_SIZE + "*" + ConstDB.COLUMN_AMOUNT + ") AS " + ConstDB.TOTAL_EXPENSE + ","
                        + ConstDB.COLUMN_FORMAT + ","
                        + ConstDB.COLUMN_NAMEM +
                " FROM " + ConstDB.TABLE_EXPENSE_MAT +
                        " INNER JOIN " + ConstDB.TABLE_MATERIALS +
                            " ON " + ConstDB.COLUMN_IDM + "=" + ConstDB.COLUMN_IDME +
                        " INNER JOIN " + ConstDB.TABLE_COST_ACT +
                            " ON " + ConstDB.COLUMN_IDAC + "=" + ConstDB.COLUMN_IDAE +
                        " INNER JOIN " + ConstDB.TABLE_PROJECTS +
                            " ON " + ConstDB.COLUMN_IDP + "=" + ConstDB.COLUMN_IDPC +
                " WHERE " + ConstDB.COLUMN_IDP + "=?" +
                " GROUP BY " + ConstDB.COLUMN_IDM;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, arg);

        if (cursor.moveToFirst()){
            result = new String[cursor.getCount()];
            BigDecimal formater;

            for (int i = 0; i < result.length; i++){
                formater = BigDecimal.valueOf(cursor.getDouble(0));
                formater = formater.setScale(0, BigDecimal.ROUND_UP);

                result[i] = formater.intValue() + " " + cursor.getString(1) + " " + cursor.getString(2);
                cursor.moveToNext();
            }

            cursor.close();
        }

        db.close();
        return  result;
    }

    String[] calculateMaterialExpenseByRoom(int idPro, String room){
        String[] result = null;
        String[] arg = new String[]{Integer.toString(idPro), room};

        String query =
                "SELECT SUM(" + ConstDB.COLUMN_SIZE + "*" + ConstDB.COLUMN_AMOUNT + ") AS " + ConstDB.TOTAL_EXPENSE + ","
                        + ConstDB.COLUMN_FORMAT + ","
                        + ConstDB.COLUMN_NAMEM +
                " FROM " + ConstDB.TABLE_EXPENSE_MAT +
                    " INNER JOIN " + ConstDB.TABLE_MATERIALS +
                        " ON " + ConstDB.COLUMN_IDM + "=" + ConstDB.COLUMN_IDME +
                    " INNER JOIN " + ConstDB.TABLE_COST_ACT +
                        " ON " + ConstDB.COLUMN_IDAC + "=" + ConstDB.COLUMN_IDAE +
                    " INNER JOIN " + ConstDB.TABLE_PROJECTS +
                        " ON " + ConstDB.COLUMN_IDP + "=" + ConstDB.COLUMN_IDPC +
                " WHERE " + ConstDB.COLUMN_IDP + "=? AND " + ConstDB.COLUMN_ROOM + "=?" +
                " GROUP BY " + ConstDB.COLUMN_IDM;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, arg);

        if (cursor.moveToFirst()){
            result = new String[cursor.getCount()];
            BigDecimal formater;

            for (int i = 0; i < result.length; i++){
                formater = BigDecimal.valueOf(cursor.getDouble(0));
                formater = formater.setScale(0, BigDecimal.ROUND_UP);

                result[i] = formater.intValue() + " " + cursor.getString(1) + " " + cursor.getString(2);
                cursor.moveToNext();
            }

            cursor.close();
        }

        db.close();
        return  result;
    }

    String[] calculateMaterialExpenseByAct(int idPro, int idAct){
        String[] result = null;
        String[] arg = new String[]{Integer.toString(idPro), Integer.toString(idAct)};

        String query =
                "SELECT SUM(" + ConstDB.COLUMN_SIZE + "*" + ConstDB.COLUMN_AMOUNT + ") AS " + ConstDB.TOTAL_EXPENSE + ","
                        + ConstDB.COLUMN_FORMAT + ","
                        + ConstDB.COLUMN_NAMEM +
                " FROM " + ConstDB.TABLE_EXPENSE_MAT +
                    " INNER JOIN " + ConstDB.TABLE_MATERIALS +
                        " ON " + ConstDB.COLUMN_IDM + "=" + ConstDB.COLUMN_IDME +
                    " INNER JOIN " + ConstDB.TABLE_COST_ACT +
                        " ON " + ConstDB.COLUMN_IDAC + "=" + ConstDB.COLUMN_IDAE +
                    " INNER JOIN " + ConstDB.TABLE_PROJECTS +
                        " ON " + ConstDB.COLUMN_IDP + "=" + ConstDB.COLUMN_IDPC +
                " WHERE " + ConstDB.COLUMN_IDP + "=? AND " + ConstDB.COLUMN_IDAC + "=?" +
                " GROUP BY " + ConstDB.COLUMN_IDM;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, arg);

        if (cursor.moveToFirst()){
            result = new String[cursor.getCount()];
            BigDecimal formater;

            for (int i = 0; i < result.length; i++){
                formater = BigDecimal.valueOf(cursor.getDouble(0));
                formater = formater.setScale(0, BigDecimal.ROUND_UP);

                result[i] = formater.intValue() + " " + cursor.getString(1) + " " + cursor.getString(2);
                cursor.moveToNext();
            }

            cursor.close();
        }

        db.close();
        return  result;
    }

    void insertExpenseMaterial(ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ConstDB.TABLE_EXPENSE_MAT, null, values);
        db.close();
    }

    void editExpenseMaterial(ContentValues editedValues, int idAct, int idMat){
        String[] arg = new String[]{Integer.toString(idAct), Integer.toString(idMat)};

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(ConstDB.TABLE_EXPENSE_MAT, editedValues, ConstDB.COLUMN_IDAE + "=? AND " + ConstDB.COLUMN_IDME + "=?", arg);
        db.close();
    }

    void deleteDirectExpenseMaterial(int idAct, int idMat){
        String[] arg = new String[]{Integer.toString(idAct), Integer.toString(idMat)};

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ConstDB.TABLE_EXPENSE_MAT, ConstDB.COLUMN_IDAE + "=? AND " + ConstDB.COLUMN_IDME + "=?", arg);
        db.close();
    }

    void deleteIndirectExpenseMaterialByMat(int idMat){
        String[] arg = new String[]{Integer.toString(idMat)};

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ConstDB.TABLE_EXPENSE_MAT, ConstDB.COLUMN_IDME + "=?", arg);
        db.close();
    }

    void deleteIndirectExpenseMaterialbyAct(int idAct){
        String[] arg = new String[]{Integer.toString(idAct)};

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ConstDB.TABLE_EXPENSE_MAT, ConstDB.COLUMN_IDAE + "=?", arg);
        db.close();
    }

    boolean existElementInExpenseMat(int idAct, int idMat){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] col = new String[]{ConstDB.COLUMN_IDAE, ConstDB.COLUMN_IDME};
        String[] arg = new String[]{Integer.toString(idAct), Integer.toString(idMat)};

        Cursor cursor = db.query(
                ConstDB.TABLE_EXPENSE_MAT, col,
                ConstDB.COLUMN_IDAE + "=? AND " + ConstDB.COLUMN_IDME + "=?", arg,
                null, null, null);

        boolean result = cursor.moveToFirst();

        cursor.close();
        db.close();

        return result;
    }


//    Cost's Methods
    ArrayList<CostActExtended> getCostActivitesByProjects(int idPro){
        ArrayList<CostActExtended> costActExtendeds = new ArrayList<>();
        String[] arg = new String[]{Integer.toString(idPro)};

        String query =
                "SELECT "
                        + ConstDB.COLUMN_IDAC + ","
                        + ConstDB.COLUMN_SIZE + ","
                        + ConstDB.COLUMN_ROOM + ","
                        + ConstDB.COLUMN_DUAL + ","
                        + ConstDB.COLUMN_PROGRESS + ","
                        + ConstDB.COLUMN_NAMEA + ","
                        + ConstDB.COLUMN_UNIT +
                " FROM "
                        + ConstDB.TABLE_COST_ACT +
                    " INNER JOIN "
                        + ConstDB.TABLE_ACTIVITIES +
                    " ON "
                        + ConstDB.COLUMN_IDAC + "=" + ConstDB.COLUMN_IDA +
                " WHERE "
                        + ConstDB.COLUMN_IDPC + "=?" +
                " GROUP BY "
                        + ConstDB.COLUMN_IDAC;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, arg);

        if (cursor.moveToFirst()){
            do {
                CostAct costAct = new CostAct(
                        idPro,
                        cursor.getInt(0),
                        cursor.getDouble(1),
                        cursor.getString(2),
                        cursor.getInt(3) > 0,
                        cursor.getDouble(4));

                costActExtendeds.add(
                        new CostActExtended(
                                cursor.getString(5), cursor.getString(6), costAct));

            }while (cursor.moveToNext());

            cursor.close();
        }
        db.close();
        return costActExtendeds;
    }

    ArrayList<CostActExtended> getCostActDualByProjects(int idPro){
        ArrayList<CostActExtended> costActExtendeds = new ArrayList<>();
        String[] arg = new String[]{Integer.toString(idPro), Integer.toString(1)};

        String query =
                "SELECT "
                        + ConstDB.COLUMN_IDAC + ","
                        + ConstDB.COLUMN_SIZE + ","
                        + ConstDB.COLUMN_ROOM + ","
//                        + ConstDB.COLUMN_DUAL + ","
                        + ConstDB.COLUMN_PROGRESS + ","
                        + ConstDB.COLUMN_NAMEA + ","
                        + ConstDB.COLUMN_UNIT +
                " FROM "
                        + ConstDB.TABLE_COST_ACT +
                    " INNER JOIN "
                        + ConstDB.TABLE_ACTIVITIES +
                    " ON "
                        + ConstDB.COLUMN_IDAC + "=" + ConstDB.COLUMN_IDA +
                " WHERE "
                        + ConstDB.COLUMN_IDPC + "=? AND "
                        + ConstDB.COLUMN_DUAL + "=?" +
                "GROUP BY "
                        + ConstDB.COLUMN_IDAC;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, arg);

        if (cursor.moveToFirst()){
            do {
                CostAct costAct = new CostAct(
                        idPro,
                        cursor.getInt(0),
                        cursor.getDouble(1),
                        cursor.getString(2),
                        true, cursor.getDouble(3));

                costActExtendeds.add(
                        new CostActExtended(
                                cursor.getString(4), cursor.getString(5), costAct));

            }while (cursor.moveToNext());

            cursor.close();
        }
        db.close();
        return costActExtendeds;
    }

    ArrayList<CostActExtended> getCostActivitiesByRoom(int idPro, String room){
        ArrayList<CostActExtended> costActExtendeds = new ArrayList<>();
        String[] arg = new String[]{Integer.toString(idPro),room};

         String query =
                "SELECT "
                        + ConstDB.COLUMN_IDAC + ","
                        + ConstDB.COLUMN_SIZE + ","
                        + ConstDB.COLUMN_DUAL + ","
                        + ConstDB.COLUMN_PROGRESS + ","
                        + ConstDB.COLUMN_NAMEA + ","
                        + ConstDB.COLUMN_UNIT +

                " FROM "
                        + ConstDB.TABLE_COST_ACT +
                    " INNER JOIN "
                        + ConstDB.TABLE_ACTIVITIES +
                    " ON "
                        + ConstDB.COLUMN_IDAC + "=" + ConstDB.COLUMN_IDA +

                " WHERE "
                        + ConstDB.COLUMN_IDPC + "=? AND "
                        + ConstDB.COLUMN_ROOM + "=?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, arg);

        if (cursor.moveToFirst()){
            do {
                CostAct costAct = new CostAct(
                        idPro,
                        cursor.getInt(0),
                        cursor.getDouble(1),
                        room,
                        cursor.getInt(2) > 0,
                        cursor.getDouble(3));

                costActExtendeds.add(
                        new CostActExtended(
                                cursor.getString(4), cursor.getString(5), costAct));

            }while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return costActExtendeds;
    }

    ArrayList<String> getRoomsOfPro(int idPro){
        ArrayList<String> rooms = new ArrayList<>();
        String[] col = new String[]{ConstDB.COLUMN_ROOM};
        String[] arg = new String[]{Integer.toString(idPro)};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                ConstDB.TABLE_COST_ACT, col,
                ConstDB.COLUMN_IDPC + "=?", arg,
                ConstDB.COLUMN_ROOM, null,
                ConstDB.COLUMN_ROOM + " ASC");

        if (cursor.moveToFirst()){
            do {
                rooms.add(cursor.getString(0));
            }while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return rooms;
    }

    ArrayList<String> getUsesProject(int idAct){
        ArrayList<String> names = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] arg = new String[]{Integer.toString(idAct)};

        String query =
                "SELECT "
                        + ConstDB.COLUMN_NAMEP +
                " FROM "
                        + ConstDB.TABLE_PROJECTS +
                    " INNER JOIN "
                        + ConstDB.TABLE_COST_ACT +
                    " ON "
                        + ConstDB.COLUMN_IDP + "=" + ConstDB.COLUMN_IDPC +
                " WHERE "
                        + ConstDB.COLUMN_IDAC + "=?" +
                " GROUP BY "
                        + ConstDB.COLUMN_IDPC +
                " ORDER BY "
                        + ConstDB.COLUMN_NAMEP + " ASC";

        Cursor cursor = db.rawQuery(query, arg);

        if (cursor.moveToFirst()){
            do{
                names.add(cursor.getString(0));
            }while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return names;
    }

    ArrayList<Integer> getIdsAssignedAct(int idPro){
        ArrayList<Integer> ids = new ArrayList<>();
        String[] col = new String[]{ConstDB.COLUMN_IDAC};
        String[] arg = new String[]{Integer.toString(idPro)};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                ConstDB.TABLE_COST_ACT, col,
                ConstDB.COLUMN_IDPC + "=?", arg,
                null, null, null);

        if (cursor.moveToFirst()){
            do {
                ids.add(cursor.getInt(0));
            }while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return ids;
    }

    double getSingleSize(int idPro, int idAct, String room){
        double result = 0;
        String[] col = new String[]{ConstDB.COLUMN_SIZE};
        String[] arg = new String[]{Integer.toString(idPro), Integer.toString(idAct), room};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                ConstDB.TABLE_COST_ACT, col,
                ConstDB.COLUMN_IDPC + "=? AND " + ConstDB.COLUMN_IDAC + "=? AND " + ConstDB.COLUMN_ROOM + "=?", arg,
                null, null, null);

        if (cursor.moveToFirst()){
            result = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return result;
    }

    double getProgressOfProject(int idPro){
        double result = 0;
        String[] arg = new String[]{Integer.toString(idPro)};

        String query =
                "SELECT AVG(" + ConstDB.COLUMN_PROGRESS + "/" + ConstDB.COLUMN_SIZE + ") AS " + ConstDB.DECIMAL_PROGRESS +
                " FROM " + ConstDB.TABLE_COST_ACT +
                " WHERE " + ConstDB.COLUMN_IDPC + "=?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, arg);

        if (cursor.moveToFirst())result = cursor.getDouble(0);
        cursor.close();
        db.close();
        return result;
    }

    double getProgressOfActivity(int idPro, int idAct){
        double result = 0;
        String[] arg = new String[]{Integer.toString(idPro), Integer.toString(idAct)};

        String query =
                "SELECT (" + ConstDB.COLUMN_PROGRESS + "/" + ConstDB.COLUMN_SIZE + ") AS " + ConstDB.DECIMAL_PROGRESS +
                " FROM " + ConstDB.TABLE_COST_ACT +
                " WHERE " + ConstDB.COLUMN_IDPC + "=? AND " + ConstDB.COLUMN_IDAC + "=?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, arg);

        if (cursor.moveToFirst())result = cursor.getDouble(0);
        cursor.close();
        db.close();
        return result;
    }

    int countCostActByPro(int idPro){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] col = new String[]{ConstDB.COLUMN_IDAC};
        String[] arg = new String[]{Integer.toString(idPro)};

        Cursor cursor = db.query(
                ConstDB.TABLE_COST_ACT, col,
                ConstDB.COLUMN_IDPC + "=?", arg,
                null, null, null);

        int result = cursor.getCount();
        cursor.close();
        return result;
    }

    int countActivitiesFinished(int idPro){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] col = new String[]{ConstDB.COLUMN_IDAC};
        String[] arg = new String[]{Integer.toString(idPro)};

        Cursor cursor = db.query(
                ConstDB.TABLE_COST_ACT,col,
                ConstDB.COLUMN_IDPC + "=? AND " + ConstDB.COLUMN_PROGRESS + "==" + ConstDB.COLUMN_SIZE, arg,
                null,null,null);

        int result = cursor.getCount();
        cursor.close();
        return result;
    }

    int calculateTotalCost(int idPro){
        int result = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] arg = new String[]{Integer.toString(idPro), Integer.toString(1)};

        String query =
                "SELECT "
                        + ConstDB.COLUMN_SIZE + "*" + ConstDB.COLUMN_PRICE +
                    " AS "
                        + ConstDB.TOTAL_COST +
                " FROM "
                        + ConstDB.TABLE_COST_ACT +
                    " INNER JOIN "
                        + ConstDB.TABLE_ACTIVITIES +
                    " ON "
                        + ConstDB.COLUMN_IDAC + "=" + ConstDB.COLUMN_IDA +
                " WHERE "
                    + ConstDB.COLUMN_IDPC + "=? AND "
                    + ConstDB.COLUMN_DUAL + "=?";

        Cursor cursor = db.rawQuery(query, arg);

        if (cursor.moveToFirst()){
            do {
                result += (int) Math.round(cursor.getDouble(0));
            }while (cursor.moveToNext());

            if (result % 10 != 0)result += (10 - result % 10);
            cursor.close();
        }

        db.close();
        return result;
    }

    int calculateTotalCostByRoom(int idPro, String room){
        int result = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] arg = new String[]{Integer.toString(idPro), Integer.toString(1), room};

        String query =
                "SELECT "
                        + ConstDB.COLUMN_SIZE + "*" + ConstDB.COLUMN_PRICE +
                    " AS "
                        + ConstDB.TOTAL_COST +
                " FROM "
                        + ConstDB.TABLE_COST_ACT +
                    " INNER JOIN "
                        + ConstDB.TABLE_ACTIVITIES +
                    " ON "
                        + ConstDB.COLUMN_IDAC + "=" + ConstDB.COLUMN_IDA +
                " WHERE "
                    + ConstDB.COLUMN_IDPC + "=? AND "
                    + ConstDB.COLUMN_DUAL + "=? AND "
                    + ConstDB.COLUMN_ROOM + "=?";

        Cursor cursor = db.rawQuery(query, arg);

        if (cursor.moveToFirst()){
            do {
                result += (int) Math.round(cursor.getDouble(0));
            }while (cursor.moveToNext());

            cursor.close();
        }
        db.close();
        return result;
    }

    int calculateTotalCostByAct(int idPro, int idAct){
        int result = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] arg = new String[]{Integer.toString(idPro), Integer.toString(idAct),Integer.toString(1)};

        String query =
                "SELECT "
                        + ConstDB.COLUMN_SIZE + "*" + ConstDB.COLUMN_PRICE +
                    " AS "
                        + ConstDB.TOTAL_COST +
                " FROM "
                        + ConstDB.TABLE_COST_ACT +
                    " INNER JOIN "
                        + ConstDB.TABLE_ACTIVITIES +
                    " ON "
                        + ConstDB.COLUMN_IDAC + "=" + ConstDB.COLUMN_IDA +
                " WHERE "
                    + ConstDB.COLUMN_IDPC + "=? AND "
                    + ConstDB.COLUMN_IDAC + "=? AND "
                    + ConstDB.COLUMN_DUAL + "=?";

        Cursor cursor = db.rawQuery(query, arg);

        if (cursor.moveToFirst()){
            do {
                result += (int) Math.round(cursor.getDouble(0));
            }while (cursor.moveToNext());

            cursor.close();
        }
        db.close();
        return result;
    }

    void insertCostActivity(ContentValues newValues){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ConstDB.TABLE_COST_ACT, null, newValues);
        db.close();
    }

    void editCostActivity(ContentValues editedValues, int idPro, int idAct){
        String[] arg = new String[]{Integer.toString(idPro), Integer.toString(idAct)};

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(
                ConstDB.TABLE_COST_ACT, editedValues,
                ConstDB.COLUMN_IDPC + "=? AND " + ConstDB.COLUMN_IDAC + "=?", arg);
        db.close();
    }

    void editCostActivityByRoom(ContentValues editedValues, int idPro, int idAct, String room){
        String[] arg = new String[]{Integer.toString(idPro), Integer.toString(idAct), room};

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(
                ConstDB.TABLE_COST_ACT, editedValues,
                ConstDB.COLUMN_IDPC + "=? AND " + ConstDB.COLUMN_IDAC + "=? AND " + ConstDB.COLUMN_ROOM + "=?", arg);
        db.close();
    }

    void deleteDirectCostActivity(int idPro, int idAct, String room){
        String[] arg = new String[]{Integer.toString(idPro), Integer.toString(idAct), room};

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ConstDB.TABLE_COST_ACT,
                ConstDB.COLUMN_IDPC + "=? AND " + ConstDB.COLUMN_IDAC + "=? AND " + ConstDB.COLUMN_ROOM + "=?", arg);
        db.close();
    }

    void deleteIndirectCostActivitybyPro(int idPro){
        String[] arg = new String[]{Integer.toString(idPro)};

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ConstDB.TABLE_COST_ACT, ConstDB.COLUMN_IDPC + "=?",arg);
        db.close();
    }

    void deleteIndirectCostActivitybyAct(int idAct){
        String[] arg = new String[]{Integer.toString(idAct)};

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ConstDB.TABLE_COST_ACT, ConstDB.COLUMN_IDAC + "=?", arg);
        db.close();
    }

//    Note's Methods
    ArrayList<Note> getNotes(int idP){
        ArrayList<Note> listNotes = new ArrayList<>();
        String[] arg = new String[]{Integer.toString(idP)};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                ConstDB.TABLE_NOTES, null,
                ConstDB.COLUMN_IDPN + "=?", arg,
                null, null, ConstDB.COLUMN_DATE + " DESC");

        if (cursor.moveToFirst()){
            do {
                listNotes.add(new Note(idP, cursor.getInt(1), new Date(cursor.getLong(2)), cursor.getString(3)));
            }while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return listNotes;
    }

    void insertNote(ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ConstDB.TABLE_NOTES, null, values);
        db.close();
    }

    void editNote(ContentValues values, int idP, int idN){
        String[] arg = new String[]{Integer.toString(idP), Integer.toString(idN)};
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(ConstDB.TABLE_NOTES, values, ConstDB.COLUMN_IDPN + "=? AND " + ConstDB.COLUMN_IDN + "=?", arg);
        db.close();
    }

    void directDeleteNote(int idP, int idN){
        String[] arg = new String[]{Integer.toString(idP), Integer.toString(idN)};
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ConstDB.TABLE_NOTES, ConstDB.COLUMN_IDPN + "=? AND " + ConstDB.COLUMN_IDN + "=?", arg);
        db.close();
    }

    void indirectDeleteNote(int idP){
        String[] arg = new String[]{Integer.toString(idP)};
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ConstDB.TABLE_NOTES, ConstDB.COLUMN_IDPN + "=?", arg);
        db.close();
    }

//    Contact's Methods
    ArrayList<Contact> getContacts(int idP){
        ArrayList<Contact> contacts = new ArrayList<>();
        String[] arg = new String[]{Integer.toString(idP)};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                ConstDB.TABLE_TELEPHONE, null,
                ConstDB.COLUMN_IDPT + "=?", arg,
                null, null,
                ConstDB.COLUMN_CONTACT + " ASC");

        if (cursor.moveToFirst()){
            do {
                contacts.add(new Contact(idP, cursor.getString(1), cursor.getString(2)));
            }while (cursor.moveToNext());

            cursor.close();
        }
        db.close();
        return contacts;
    }

    Contact findContact(int idP, String tel){
        Contact contact = null;
        String[] arg = new String[]{Integer.toString(idP), tel};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                ConstDB.TABLE_TELEPHONE, null,
                ConstDB.COLUMN_IDPT + "=? AND " + ConstDB.COLUMN_TEL + "=?", arg,
                null, null, null);

        if (cursor.moveToFirst()){
            contact = new Contact(idP, tel, cursor.getString(2));
            cursor.close();
        }

        db.close();
        return contact;
    }

    void insertContact(ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ConstDB.TABLE_TELEPHONE, null, values);
        db.close();
    }

    void editContact(ContentValues values, int idP, String tel){
        String[] arg = new String[]{Integer.toString(idP), tel};
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(ConstDB.TABLE_TELEPHONE, values, ConstDB.COLUMN_IDPT + "=? AND " + ConstDB.COLUMN_TEL + "=?", arg);
        db.close();
    }

    void directDeleteContact(int idP, String tel){
        String[] arg = new String[]{Integer.toString(idP), tel};
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ConstDB.TABLE_TELEPHONE, ConstDB.COLUMN_IDPT + "=? AND " + ConstDB.COLUMN_TEL + "=?", arg);
        db.close();
    }

    void indirectDeleteContact(int idP){
        String[] arg = new String[]{Integer.toString(idP)};
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ConstDB.TABLE_TELEPHONE, ConstDB.COLUMN_IDPT + "=?", arg);
        db.close();
    }
}
