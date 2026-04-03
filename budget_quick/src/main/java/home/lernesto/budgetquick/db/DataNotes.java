package home.lernesto.budgetquick.db;


import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import home.lernesto.budgetquick.pojos.Note;

public class DataNotes{
    public static ArrayList<Note> getData(Context context, int idP){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.getNotes(idP);
    }

    public static void insert(Context context, Note note){
        ContentValues values = new ContentValues();
        values.put(ConstDB.COLUMN_IDPN, note.getIdP());
        values.put(ConstDB.COLUMN_DATE, note.getDate().getTime());
        values.put(ConstDB.COLUMN_BODY, note.getBody());

        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.insertNote(values);
    }

    public static void edit(Context context, Note note){
        ContentValues values = new ContentValues();
        values.put(ConstDB.COLUMN_BODY, note.getBody());

        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.editNote(values, note.getIdP(), note.getIdN());
    }

    public static void directDelete(Context context, int idP, int idN){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.directDeleteNote(idP, idN);
    }

    static void indirectDelete(Context context, int idP){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.indirectDeleteNote(idP);
    }
}
