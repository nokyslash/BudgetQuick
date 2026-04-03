package home.lernesto.budgetquick.utilsns;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import home.lernesto.budgetquick.fragments.ActivitiesFragment;
import home.lernesto.budgetquick.fragments.MaterialsFragment;
import home.lernesto.budgetquick.fragments.ProjectFragment;

public class CNS {
    private static final int CALL_PHONE_REQUEST_CODE = 0;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 2;

    public static ArrayList<Fragment> initializeFragmentList(){
        ArrayList<Fragment> result = new ArrayList<>();
        result.add(new ProjectFragment());
        result.add(new ActivitiesFragment());
        result.add(new MaterialsFragment());

        return result;
    }

    public static int getPosCat(String cat, String[] categories){
        int pos = 0;
        for (int i = 0; i < categories.length;i++){
            if (cat.equals(categories[i])){
                pos = i;
                i = categories.length;
            }
        }
        return pos;
    }

    public static int getPosUm(String um, String[] ums){
        int pos = 0;
        for (int i = 0; i < ums.length; i++){
            if (um.equals(ums[i])){
                pos = i;
                i = ums.length;
            }
        }
        return pos;
    }

    static String convertNameListToString(String[] names){
        StringBuilder result = new StringBuilder();
        for (String name : names) result.append(name).append("\n");
        return result.toString();
    }

    public static boolean checkStatusCallPhonePermission(Context context){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }
    public static void requestCallPhonePermission(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.CALL_PHONE}, CALL_PHONE_REQUEST_CODE);
    }

    public static boolean checkStatusReadExternalStoragePermission(Context context){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestReadExternealStoragePermission(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST_CODE);
    }

    public static boolean checkStatusWriteExternalStoragePermission(Context context){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestWriteExternealStoragePermission(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
    }

    public static boolean isExternalStorageWritable(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static boolean isExternalStorageReadable(){
        return isExternalStorageWritable() || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState());
    }
}
