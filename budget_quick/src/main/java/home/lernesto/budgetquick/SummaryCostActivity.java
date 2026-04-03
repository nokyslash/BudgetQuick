package home.lernesto.budgetquick;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import home.lernesto.budgetquick.db.DataCostAct;
import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.pojos.CostActExtended;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class SummaryCostActivity extends AppCompatActivity {
//    Visual variables
    Toolbar toolbar;
    Spinner spnMode, spnSummary;
    TextView txvCost;

//    Logical Variables
    int idP, totalCost;
    String nameP;
    ArrayList<CostActExtended> activities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_cost);

//        To link file XML
        toolbar    = findViewById(R.id.app_bar_summary_cost);
        spnMode    = findViewById(R.id.spn_mode_cost);
        spnSummary = findViewById(R.id.spn_summary_cost);
        txvCost    = findViewById(R.id.txv_cost);

//        Extract Extras
        idP       = getIntent().getIntExtra(ConstDB.COLUMN_IDP, 0);
        totalCost = getIntent().getIntExtra(ConstDB.COLUMN_TCOST, 0);
        nameP     = getIntent().getStringExtra(ConstDB.COLUMN_NAMEP);

        initializeToolbar();
        initializeSpinnerMode();

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
            toolbar.setTitle(getString(R.string.summary_cost) + " " + nameP);
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
                activities = DataCostAct.getDataDualByProject(this, idP);
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

    @SuppressLint("SetTextI18n")
    private void refresh(String mode){
        switch (mode){
            case "Total":
                txvCost.setText("$" + totalCost);
                break;

            case "Por Habitación":
                txvCost.setText("$" +
                        DataCostAct.calcTCostByRoom(
                                this, idP, spnSummary.getSelectedItem().toString()));
                break;

            case "Por Actividades":
                txvCost.setText("$" +
                        DataCostAct.calcTCostByAct(
                                this, idP, activities.get(
                                        spnSummary.getSelectedItemPosition()).getCostAct().getIdAct()));
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
