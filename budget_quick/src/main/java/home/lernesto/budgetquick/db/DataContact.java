package home.lernesto.budgetquick.db;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import home.lernesto.budgetquick.pojos.Contact;

public class DataContact {
    public static ArrayList<Contact> getData(Context context, int idP){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.getContacts(idP);
    }

    public static boolean noExsistContact(Context context, int idP, String tel){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        return db.findContact(idP, tel) == null;
    }

    public static void insert(Context context, Contact contact){
        ContentValues values = new ContentValues();
        values.put(ConstDB.COLUMN_IDPT, contact.getIdP());
        values.put(ConstDB.COLUMN_TEL, contact.getPhone());
        values.put(ConstDB.COLUMN_CONTACT, contact.getName());

        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.insertContact(values);
    }

    public static void edit(Context context, Contact contact, String tel){
        ContentValues values = new ContentValues();
        values.put(ConstDB.COLUMN_TEL, contact.getPhone());
        values.put(ConstDB.COLUMN_CONTACT, contact.getName());

        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.editContact(values, contact.getIdP(), tel);
    }

    public static void directDelete(Context context, int idP, String tel){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.directDeleteContact(idP, tel);
    }

    //Ojo con este metodo porque lo voy a cambiar a privado y es posible que no funcione
    static void indirectDelete(Context context, int idP){
        DataBaseHelperClass db = new DataBaseHelperClass(context);
        db.indirectDeleteContact(idP);
    }
}
