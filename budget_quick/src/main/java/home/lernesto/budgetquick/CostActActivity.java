package home.lernesto.budgetquick;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;
import com.scalified.fab.ActionButton;
import com.scalified.uitools.convert.DensityConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import home.lernesto.budgetquick.adapters.CostActAdapter;
import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.db.DataCostAct;
import home.lernesto.budgetquick.db.DataExpenseMat;
import home.lernesto.budgetquick.db.DataProjects;
import home.lernesto.budgetquick.pojos.CostActExtended;
import home.lernesto.budgetquick.utilsns.CNS;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class CostActActivity extends AppCompatActivity {
    private static final int FILE_CODE = 1;

//    Visual Variables
    private Toolbar toolbar;
    private Spinner rooms;
    private RecyclerView list;
    private ActionButton fab;

//    Logical Variables
    private float distance;
    private int idP;
    private String nameP;
    private boolean fixCost;
    private boolean lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cost_act);

//        To link file XML
        toolbar = findViewById(R.id.appbar_cost_act);
        rooms   = findViewById(R.id.spinner_rooms);
        list    = findViewById(R.id.rcview_cost_act_list);
        fab     = findViewById(R.id.fab_add_cost_a);

//        Extract Extras
        idP      = getIntent().getIntExtra(ConstDB.COLUMN_IDP, 0);
        nameP    = getIntent().getStringExtra(ConstDB.COLUMN_NAMEP);
        fixCost  = getIntent().getBooleanExtra(ConstDB.COLUMN_COST_TYPE, false);
        lock  = getIntent().getBooleanExtra(ConstDB.COLUMN_LOCK, false);

        builDistance();
        initializeToolbar();
        initializeRecyclerView();
        initializeSpinner();

        rooms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CostActActivity.this,InsertCostActActivity.class);
                intent.putExtra(ConstDB.COLUMN_IDP,idP);
                intent.putExtra(ConstDB.COLUMN_NAMEP,nameP);
                intent.putExtra(ConstDB.COLUMN_COST_TYPE, fixCost);
                startActivity(intent);
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                fab.moveLeft(distance);
                changeSignDistance();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!fixCost && !lock)refreshTCost();
        initializeToolbar();
        initializeSpinner();
        fab.show();
    }

    @Override
    public void onPause() {
        fab.setVisibility(View.INVISIBLE);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu_pro, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.op_cal_cost:
                if (fixCost){
                    MDNNS.upDialogShowTCost(this, DataProjects.getTCost(this, idP));
                } else {
                    intent = new Intent(CostActActivity.this, SummaryCostActivity.class);
                    intent.putExtra(ConstDB.COLUMN_IDP, idP);
                    intent.putExtra(ConstDB.COLUMN_NAMEP, nameP);
                    intent.putExtra(ConstDB.COLUMN_TCOST, DataProjects.getTCost(this, idP));
                    startActivity(intent);
                }
                return true;

            case R.id.op_cal_exp:
                intent = new Intent(CostActActivity.this, SummaryExpenseActivity.class);
                intent.putExtra(ConstDB.COLUMN_IDP, idP);
                intent.putExtra(ConstDB.COLUMN_NAMEP, nameP);
                startActivity(intent);
                return true;

            case R.id.op_contact:
                intent = new Intent(CostActActivity.this, ContactActivity.class);
                intent.putExtra(ConstDB.COLUMN_IDP, idP);
                intent.putExtra(ConstDB.COLUMN_NAMEP, nameP);
                startActivity(intent);
                return true;

            case R.id.op_notes:
                intent = new Intent(CostActActivity.this, NotesActivity.class);
                intent.putExtra(ConstDB.COLUMN_IDP, idP);
                intent.putExtra(ConstDB.COLUMN_NAMEP, nameP);
                intent.putExtra(NotesActivity.FLAG, NotesActivity.INSERT);
                startActivity(intent);
                return true;

            case R.id.op_export:
                if (rooms.getVisibility() == View.INVISIBLE)MDNNS.upNotificationEmptyProject(rooms);
                else chooseDir();
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
                exportBudgetToTxtFile(file.getAbsolutePath());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initializeToolbar(){
        if (toolbar != null){
            toolbar.setTitle(nameP + " - $" + DataProjects.getTCost(this, idP));
            setSupportActionBar(toolbar);
        }
    }

    private void initializeSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                DataCostAct.getRooms(this,idP));

        if (adapter.isEmpty()){
            rooms.setVisibility(View.INVISIBLE);
            initializeRecyclerView();
        }
        else {
            rooms.setAdapter(adapter);
            rooms.setVisibility(View.VISIBLE);
            refreshList();
        }
    }

    private void initializeRecyclerView(){
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(llm);
        list.setAdapter(new CostActAdapter(new ArrayList<CostActExtended>(), this));
    }

    public void refreshList(){
        ((CostActAdapter)(Objects.requireNonNull(list.getAdapter()))).refreshData(idP, rooms.getSelectedItem().toString());
        list.getAdapter().notifyDataSetChanged();

    }

    private void refreshTCost(){
        DataProjects.editTotalCost(this, idP, DataCostAct.calcTCost(this, idP));
    }

    public void resumeActivity(){onResume();}
    
    private void exportBudgetToTxtFile(String pathTarget){
            if (CNS.checkStatusWriteExternalStoragePermission(this)){
                try {
                    String nameFile = getString(R.string.budget_of) + " " + nameP + ".txt";
                    FileOutputStream outputStream = new FileOutputStream(
                            new File(pathTarget, nameFile));

                    outputStream.write(buildTextOfTxtFile().getBytes());
                    outputStream.close();
                    MDNNS.upNotificationSuccesfullyExporting(this, list, pathTarget);
                }
                catch (Exception e){
                    e.printStackTrace();
                    if (e instanceof FileNotFoundException) MDNNS.upNotificationInvalidDir(list);
                    if (e instanceof SecurityException) MDNNS.upNotificationSecurityFail(list);
                    if (e instanceof NullPointerException) MDNNS.upNotificationFailCreateFile(list);
                }
            }
            else {
                CNS.requestWriteExternealStoragePermission(this);
                exportBudgetToTxtFile(pathTarget);
            }
    }

    private String buildTextOfTxtFile(){
        String header = getString(R.string.cal_cost) + " $" + DataProjects.getTCost(this, idP);
        String[] materialsList = DataExpenseMat.calcExpenseTotal(this, idP);
        StringBuilder body = new StringBuilder();
        for (String s : materialsList) {
            body.append("- ").append(s).append("\n");
        }
        return header + "\n\n" + getString(R.string.cal_exp) + "\n" + body;
    }

    private void chooseDir(){
        if (CNS.checkStatusReadExternalStoragePermission(this)){
            Intent i = new Intent(getApplicationContext(), FilePickerActivity.class);
            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
            i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
            startActivityForResult(i, FILE_CODE);
        }
        else {
            CNS.requestReadExternealStoragePermission(this);
            chooseDir();
        }
    }

    private void builDistance(){
        this.distance = getResources().getConfiguration().screenWidthDp
                - DensityConverter.pxToDp(this, fab.getSize())
                - DensityConverter.pxToDp(this, getResources().getDimension(R.dimen.activity_horizontal_margin)) * 4;
    }

    private void changeSignDistance(){
        if (distance < 0)distance = Math.abs(distance);
        else distance = -(Math.abs(distance));
    }
}
