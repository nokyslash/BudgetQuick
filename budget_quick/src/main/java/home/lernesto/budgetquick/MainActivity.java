package home.lernesto.budgetquick;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import home.lernesto.budgetquick.adapters.PageAdapter;
import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.utilsns.CNS;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class MainActivity extends AppCompatActivity {
//    Logical Variables
    private static final int INTERVAL = 2000;
    private static final int FILE_CODE = 1;
    private static final int DIR_CODE = 2;

    private long timeFirstBack;

//    Visual Variables
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        To link file XML
        toolbar   = findViewById(R.id.main_toolbar);
        viewPager = findViewById(R.id.main_viewPager);
        tabLayout = findViewById(R.id.main_tabLayout);

        initializeToolbar();
        initializeViewPager();
        prepareTabLayout();
    }

    @Override
    public void onBackPressed() {
        if (timeFirstBack + INTERVAL > System.currentTimeMillis())super.onBackPressed();
        else MDNNS.upNotificationConfirmBackExit(viewPager);
        timeFirstBack = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_whats_new:
                MDNNS.upDialogWhatsNew(this);
                return true;

            case R.id.import_export_db:
                importExportDB();
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
            assert data != null;
            List<Uri> files = Utils.getSelectedFilesFromResult(data);
            for (Uri uri: files) {
                File file = Utils.getFileForUri(uri);
                if (!file.getAbsolutePath().endsWith(".db")) MDNNS.upNotificationInvalidDBFile(viewPager);
                else if (!checkDB(file.getAbsolutePath()))MDNNS.upNotificationInvalidBudgetQuickDB(viewPager);
                     else importDB(file.getAbsolutePath());
            }
        }

        if (requestCode == DIR_CODE && resultCode == Activity.RESULT_OK) {
            assert data != null;
            List<Uri> files = Utils.getSelectedFilesFromResult(data);
            for (Uri uri : files) {
                File file = Utils.getFileForUri(uri);
                exportDB(file.getAbsolutePath());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initializeToolbar(){
        if(toolbar != null) setSupportActionBar(toolbar);
    }

    private void initializeViewPager(){
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(), CNS.initializeFragmentList()));
    }

    private void prepareTabLayout(){
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(R.string.projects);
        tabLayout.getTabAt(1).setText(R.string.activities);
        tabLayout.getTabAt(2).setText(R.string.materials);
    }

    public Fragment getFragment(){return ((PageAdapter)viewPager.getAdapter()).getItem(viewPager.getCurrentItem());}

    private void importExportDB(){
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.dialog_select_action_import_export, null);
        ListView listView = view.findViewById(R.id.list_action_import_export);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.import_export, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_action)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.cancel();
                switch (position){
                    case 0:
                        importDBSelected();
                        break;
                    case 1:
                        exportDBSelected();
                        break;
                }
            }
        });
    }

    private void importDBSelected(){
        if (CNS.checkStatusReadExternalStoragePermission(this)){
            Intent i = new Intent(this, FilePickerActivity.class);
            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
            i.putExtra(
                    FilePickerActivity.EXTRA_START_PATH,
                    Environment.getExternalStorageDirectory().getPath());
            startActivityForResult(i, FILE_CODE);
        }
        else {
            CNS.requestReadExternealStoragePermission(this);
            importDBSelected();
        }
    }

    private void exportDBSelected(){
        if (CNS.checkStatusWriteExternalStoragePermission(this)){
            Intent i = new Intent(this, FilePickerActivity.class);
            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
            i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
            startActivityForResult(i, DIR_CODE);
        }
        else {
            CNS.requestWriteExternealStoragePermission(this);
            exportDBSelected();
        }
    }

    private void importDB(String path){
        if (CNS.checkStatusReadExternalStoragePermission(this)){
            File oldDB = getDatabasePath(ConstDB.DATABASE_NAME);

            try {
//                Throws FileNotFoundException
                InputStream inputStream = new FileInputStream(path);
                OutputStream outputStream = new FileOutputStream(oldDB);

//                Throws IOException
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof FileNotFoundException)MDNNS.upNotificationFileNotFound(viewPager);
                if (e instanceof IOException)MDNNS.upNotificationIOError(viewPager);
            }

        }
        else {
            CNS.requestReadExternealStoragePermission(this);
            importDB(path);
        }
    }

    private void exportDB(String path){
        if (CNS.checkStatusWriteExternalStoragePermission(this)){
            try {
//                Throws FileNotFoundException
                InputStream reader = new FileInputStream(getDatabasePath(ConstDB.DATABASE_NAME));
                OutputStream writer = new FileOutputStream(new File(path, buildFullExportDBName()));

//                Throws IOException
                byte[] buffer = new byte[1024];
                int length;
                while ((length = reader.read(buffer)) > 0) {
                    writer.write(buffer, 0, length);
                }

                writer.flush();
                writer.close();
                reader.close();
                MDNNS.upNotificationSuccesfullyExporting(this, viewPager, path);

            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof FileNotFoundException)MDNNS.upNotificationFileNotFound(viewPager);
                if (e instanceof IOException)MDNNS.upNotificationIOError(viewPager);
                if (e instanceof SecurityException) MDNNS.upNotificationSecurityFail(viewPager);
            }
        }
        else {
            CNS.requestWriteExternealStoragePermission(this);
            exportDB(path);
        }
    }

    private boolean checkDB(String path){
        boolean result = true;

        if (CNS.checkStatusReadExternalStoragePermission(this)){
            SQLiteDatabase extDB = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
            Cursor c = extDB.rawQuery(ConstDB.CHECK_TABLES, null);
            if (c.moveToFirst()){
                ArrayList<String> tables = ConstDB.getTablesInUse();
                boolean noFind = false;
                do {
                    if(!tables.contains(c.getString(0))){
                        result = false;
                        noFind = true;
                    }
                }
                while (c.moveToNext() && !noFind);
            }
            else result = false;
            c.close();
            extDB.close();
        }
        else {
            CNS.requestReadExternealStoragePermission(this);
            checkDB(path);
        }
        return result;
    }

    private String buildFullExportDBName(){
        return ConstDB.DATABASE_NAME.concat(
                "_v" + ConstDB.DATABASE_VERSION + "_").concat(
                        DateFormat.getDateTimeInstance().format(new Date())).concat(
                                ".db");
    }
}
