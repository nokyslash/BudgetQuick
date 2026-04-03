package home.lernesto.budgetquick;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.math.BigDecimal;
import java.util.ArrayList;

import home.lernesto.budgetquick.db.DataExpenseMat;
import home.lernesto.budgetquick.db.DataMaterials;
import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.pojos.ExpenseMat;
import home.lernesto.budgetquick.pojos.Material;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class EditExpenseMatActivity extends AppCompatActivity {
//    Vidual Variables
    private Toolbar toolbar;
    private Spinner spinner;
    private Button btnEdit;
    private EditText editAmount;

//    Logical Variables
    private Context context;
    private ArrayList<Material> materials;
    private int idA, idM;
    private String nameA;
    private double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_expense_mat);

//        To link file XML
        toolbar    = findViewById(R.id.appbar_insert_expense_mat);
        spinner    = findViewById(R.id.spinner_mat_names);
        btnEdit    = findViewById(R.id.button_insert_expense_mat);
        editAmount = findViewById(R.id.edit_amount);

        context    = this;

//        Extract Extras
        idA    = getIntent().getIntExtra(ConstDB.COLUMN_IDA, 0);
        idM    = getIntent().getIntExtra(ConstDB.COLUMN_IDM, 0);
        nameA  = getIntent().getStringExtra(ConstDB.COLUMN_NAMEA);
        amount = getIntent().getDoubleExtra(ConstDB.COLUMN_AMOUNT, 0.0);

        initializeToolbar();
        initializeSpinner();
        initializeButton();
        initilizeEditText();

        btnEdit.setOnClickListener(v -> {
            if (!editAmount.getText().toString().isEmpty())
                if (materials.get(spinner.getSelectedItemPosition()).getIdMaterial() == idM) edit(v);
                else if (DataExpenseMat.existElement(
                        context, idA, materials.get(spinner.getSelectedItemPosition()).getIdMaterial()))
                    MDNNS.upNotificationAlreadyExist(v);
                else edit(v);
            else MDNNS.upNotificationEmptyField(v);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initializeToolbar(){
        if(toolbar != null){
            toolbar.setTitle(getString(R.string.edit_expense_mat_to) + " " + nameA);
            setSupportActionBar(toolbar);
        }
    }

    private void initializeSpinner(){
        materials = DataMaterials.getData(this);
        String[] names = new String[materials.size()];

        for (int i = 0; i < materials.size(); i ++){
            names[i] = materials.get(i).getNameMaterial() + " - " + materials.get(i).getFormat();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, names);
        spinner.setAdapter(adapter);

        int pos = 0;
        for (int i = 0; i < materials.size(); i ++){
            if(materials.get(i).getIdMaterial() == idM)pos = i;
        }

        spinner.setSelection(pos);
    }

    private void initializeButton(){
        btnEdit.setText(R.string.edit);
    }

    @SuppressLint("SetTextI18n")
    private void initilizeEditText(){
        editAmount.setText(Double.toString(amount));
    }

    private void hideVirtualKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        assert imm != null;
        imm.hideSoftInputFromWindow(btnEdit.getWindowToken(), 0);
    }

    private void edit(View v){
        DataExpenseMat.edit(
                this,
                new ExpenseMat(
                        idA,
                        materials.get(spinner.getSelectedItemPosition()).getIdMaterial(),
                        foratDoubleToTwoDigits(Double.parseDouble(editAmount.getText().toString()))),
                idM);
        hideVirtualKeyboard();
        MDNNS.upNotificationEdited(this, v);
    }

    private double foratDoubleToTwoDigits(double number){
        BigDecimal formater = new BigDecimal(number);
        formater = formater.setScale(4, BigDecimal.ROUND_HALF_UP);
        return formater.doubleValue();
    }
}
