package home.lernesto.budgetquick;

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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.math.BigDecimal;
import java.util.ArrayList;

import home.lernesto.budgetquick.db.DataExpenseMat;
import home.lernesto.budgetquick.db.DataMaterials;
import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.pojos.ExpenseMat;
import home.lernesto.budgetquick.pojos.Material;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class InsertExpenseMatActivity extends AppCompatActivity {
    //    Visual Variables
    private Context context;
    private Toolbar toolbar;
    private Spinner spinner;
    private EditText editAmount;
    private Button btnInsert;
    private ArrayList<Material> materials;

//    Logical Variables
    private int idA;
    private String nameA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_expense_mat);

//        To link file XML
        toolbar    = findViewById(R.id.appbar_insert_expense_mat);
        spinner    = findViewById(R.id.spinner_mat_names);
        btnInsert  = findViewById(R.id.button_insert_expense_mat);
        editAmount = findViewById(R.id.edit_amount);

        context = this;

//        Extract Extras
        idA   = getIntent().getIntExtra(ConstDB.COLUMN_IDA, 0);
        nameA = getIntent().getStringExtra(ConstDB.COLUMN_NAMEA);

        initializeToolbar();
        initializeSpinner();
        initializeButton();

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.isEnabled()){
                    if (!editAmount.getText().toString().isEmpty()) {
                        if (!DataExpenseMat.existElement(context, idA,
                            materials.get(spinner.getSelectedItemPosition()).getIdMaterial())) insert(v);
                        else MDNNS.upNotificationAlreadyExist(v);
                    } else MDNNS.upNotificationEmptyField(v);
                }else MDNNS.upNotificationThereIsNotMat(v);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initializeToolbar(){
        if(toolbar != null){
            toolbar.setTitle(getString(R.string.expense_mat_to) + " " + nameA);
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
        if (adapter.isEmpty())spinner.setEnabled(false);
        else {
            spinner.setEnabled(true);
            spinner.setAdapter(adapter);
        }
    }

    private void initializeButton(){
        btnInsert.setText(R.string.insert);
    }

    private void hideVirtualKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        assert imm != null;
        imm.hideSoftInputFromWindow(editAmount.getWindowToken(), 0);
    }

    private void reset(){
        YoYo.with(Techniques.Shake).playOn(findViewById(R.id.edit_amount));
        spinner.setSelection(0);
        editAmount.setText("");
    }

    private void insert(View v){
        hideVirtualKeyboard();
        DataExpenseMat.insert(
                this,
                new ExpenseMat(
                        idA,
                        materials.get(spinner.getSelectedItemPosition()).getIdMaterial(),
                        foratDoubleToTwoDigits(Double.parseDouble(editAmount.getText().toString()))));
        reset();
        MDNNS.upNotificationInserted(v);
    }

    private double foratDoubleToTwoDigits(double number){
        BigDecimal formater = new BigDecimal(number);
        formater = formater.setScale(4, BigDecimal.ROUND_HALF_UP);
        return formater.doubleValue();
    }
}
