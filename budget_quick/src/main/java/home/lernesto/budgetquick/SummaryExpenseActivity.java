package home.lernesto.budgetquick;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import home.lernesto.budgetquick.adapters.SummaryExpenseAdapter;
import home.lernesto.budgetquick.db.DataCostAct;
import home.lernesto.budgetquick.db.DataExpenseMat;
import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.pojos.CostActExtended;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class SummaryExpenseActivity extends AppCompatActivity {
//    Visual Variables
    private Toolbar toolbar;
    private Spinner spnMode, spnSummary;
    private RecyclerView recyclerView;

//    Logical Variables
    int idP;
    String nameP;
    ArrayList<CostActExtended> activities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_expense);

//        To link file XML
        toolbar      = findViewById(R.id.app_bar_summary_exp);
        spnMode      = findViewById(R.id.spn_mode_exp);
        spnSummary   = findViewById(R.id.spn_summary_exp);
        recyclerView = findViewById(R.id.rcv_items);

//        Extract Extras
        idP   = getIntent().getIntExtra(ConstDB.COLUMN_IDP, 0);
        nameP = getIntent().getStringExtra(ConstDB.COLUMN_NAMEP);

        initializeToolbar();
        initializeSpinnerMode();
        initialiezeRecyclerView();

        spnMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshSpinnerSummary(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnSummary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refresh(spnMode.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initializeToolbar(){
        if (toolbar != null)
            toolbar.setTitle(getString(R.string.summary_expense) + " " + nameP);
    }

    private void initializeSpinnerMode(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.mode, R.layout.support_simple_spinner_dropdown_item);

        spnMode.setAdapter(adapter);
        refreshSpinnerSummary(spnMode.getSelectedItem().toString());
    }

    private void refreshSpinnerSummary(String mode){
        switch (mode){
            case "Total":
                spnSummary.setVisibility(View.INVISIBLE);
                refresh(mode);
                break;

            case "Por Habitación":
                ArrayAdapter<String> adapterSummary = new ArrayAdapter<>(
                        this, R.layout.support_simple_spinner_dropdown_item, DataCostAct.getRooms(this, idP));

                if (!adapterSummary.isEmpty()){
                    spnSummary.setVisibility(View.VISIBLE);
                    spnSummary.setAdapter(adapterSummary);
                    refresh(mode);
                } else {
                    spnSummary.setVisibility(View.INVISIBLE);
                    MDNNS.upNotificationThereIsNotElements(spnSummary);
                }
                break;

            case "Por Actividades":
                activities = DataCostAct.getDataByProject(this, idP);
                 adapterSummary = new ArrayAdapter<>(
                        this, R.layout.support_simple_spinner_dropdown_item, extractActNames());

                if (!adapterSummary.isEmpty()){
                    spnSummary.setVisibility(View.VISIBLE);
                    spnSummary.setAdapter(adapterSummary);
                    refresh(mode);
                } else {
                    spnSummary.setVisibility(View.INVISIBLE);
                    MDNNS.upNotificationThereIsNotElements(spnSummary);
                }
                break;
        }
    }

    private void initialiezeRecyclerView(){
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        refresh(spnMode.getSelectedItem().toString());
    }

    private void refresh(String mode){
        switch (mode){
            case "Total":
                String[] data = DataExpenseMat.calcExpenseTotal(this, idP);
                if (data != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new SummaryExpenseAdapter(data));
                } else {
                    recyclerView.setVisibility(View.INVISIBLE);
                    MDNNS.upNotificationThereIsNotElements(spnSummary);
                }
                break;

            case "Por Habitación":
                data = DataExpenseMat.calcExpenseByRoom(this, idP, spnSummary.getSelectedItem().toString());
                if (data != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new SummaryExpenseAdapter(data));
                } else {
                    recyclerView.setVisibility(View.INVISIBLE);
                    MDNNS.upNotificationThereIsNotElements(spnSummary);
                }
                break;

            case "Por Actividades":
                data = DataExpenseMat.calcExpenseByAct(
                        this, idP, activities.get(spnSummary.getSelectedItemPosition()).getCostAct().getIdAct());
                if (data != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new SummaryExpenseAdapter(data));
                } else {
                    recyclerView.setVisibility(View.INVISIBLE);
                    MDNNS.upNotificationThereIsNotElements(spnSummary);
                }
                break;
        }
    }

    private String[] extractActNames(){
        String[] result = new String[activities.size()];

        for (int i= 0; i < result.length; i++ )
            result[i] = activities.get(i).getNameAct();

        return result;
    }
}
