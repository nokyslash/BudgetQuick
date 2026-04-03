package home.lernesto.budgetquick;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.db.DataActivity;
import home.lernesto.budgetquick.db.DataCostAct;
import home.lernesto.budgetquick.pojos.Activity;
import home.lernesto.budgetquick.utilsns.CNS;
import home.lernesto.budgetquick.utilsns.MDNNS;


public class EditActActivity extends AppCompatActivity {
//    Visual Variables
    private Toolbar toolbar;
    private Spinner cat, ums;
    private EditText editName, editPrice;
    private Button btnEdit;

//    Logical Variables
    private int idA;
    private String nameA, category, um;
    private double price;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_act);

//        To link file XML
        toolbar   = findViewById(R.id.appbar_insert_act);
        editName  = findViewById(R.id.edit_name_act);
        editPrice = findViewById(R.id.edit_price);
        cat       = findViewById(R.id.spinner_cat_insert);
        ums       = findViewById(R.id.spinner_um);
        btnEdit   = findViewById(R.id.button_ok_insert_act);

//        Extract Extras
        idA      = getIntent().getIntExtra(ConstDB.COLUMN_IDA, 0);
        nameA    = getIntent().getStringExtra(ConstDB.COLUMN_NAMEA);
        category = getIntent().getStringExtra(ConstDB.COLUMN_CAT);
        um       = getIntent().getStringExtra(ConstDB.COLUMN_UNIT);
        price    = getIntent().getIntExtra(ConstDB.COLUMN_PRICE, 0);

        initializeToolbar();
        initializeSpinner();
        initializeEditText();
        initiliazeButton();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editName.getText().toString().isEmpty() && !editPrice.getText().toString().isEmpty()) edit(v);
                else MDNNS.upNotificationEmptyField(v);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initializeToolbar(){
        if (toolbar != null){
            toolbar.setTitle(R.string.edit_act);
            setSupportActionBar(toolbar);
        }
    }

    private void initializeSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.categories,
                R.layout.support_simple_spinner_dropdown_item
        );
        cat.setAdapter(adapter);
        cat.setSelection(CNS.getPosCat(category, getResources().getStringArray(R.array.categories)));

        adapter = ArrayAdapter.createFromResource(this,R.array.ums,R.layout.support_simple_spinner_dropdown_item);
        ums.setAdapter(adapter);
        ums.setSelection(CNS.getPosUm(um, getResources().getStringArray(R.array.ums)));

        if (DataCostAct.isUsed(this,idA) || DataCostAct.containsExpenseMat(this,idA)) ums.setEnabled(false);
    }

    @SuppressLint("SetTextI18n")
    private void initializeEditText(){
        editName.setText(nameA);
        editPrice.setText(Double.toString(price));
    }

    private void initiliazeButton(){
        btnEdit.setText(R.string.edit);
    }

    private void hideVirtualKeyBoard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        assert imm != null;
        imm.hideSoftInputFromWindow(editName.getWindowToken(),0);
        imm.hideSoftInputFromWindow(editPrice.getWindowToken(), 0);
    }

    private void edit(View v){
        hideVirtualKeyBoard();
        DataActivity.edit(
                this, new Activity(
                        idA,
                        editName.getText().toString(),
                        cat.getSelectedItem().toString(),
                        ums.getSelectedItem().toString(),
                        Integer.parseInt(editPrice.getText().toString())));
        MDNNS.upNotificationEdited(this, v);
    }
}