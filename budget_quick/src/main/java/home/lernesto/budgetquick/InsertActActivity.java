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

import home.lernesto.budgetquick.db.DataActivity;
import home.lernesto.budgetquick.pojos.Activity;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class InsertActActivity extends AppCompatActivity {
//    Visual Variables
    private Toolbar toolbar;
    private Spinner cat, ums;
    private EditText editName, editPrice;
    private Button btnInsert;

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
        btnInsert = findViewById(R.id.button_ok_insert_act);

        initializeToolbar();
        initializeSpinner();
        initializeButton();

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editName.getText().toString().isEmpty() && !editPrice.getText().toString().isEmpty()) insert(v);
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
            toolbar.setTitle(R.string.new_act);
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

        adapter = ArrayAdapter.createFromResource(this,R.array.ums,R.layout.support_simple_spinner_dropdown_item);
        ums.setAdapter(adapter);
    }

    private void initializeButton(){
        btnInsert.setText(R.string.insert);
    }

    private void hideVirtualKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        assert imm != null;
        imm.hideSoftInputFromWindow(editName.getWindowToken(),0);
        imm.hideSoftInputFromWindow(editPrice.getWindowToken(), 0);
    }

    private void reset(){
        YoYo.with(Techniques.Shake).playOn(findViewById(R.id.edit_name_act));
        YoYo.with(Techniques.Shake).playOn(findViewById(R.id.edit_price));
        editName.setText("");
        editPrice.setText("");
        cat.setSelection(0);
        ums.setSelection(0);
    }

    private void insert(View v){
        DataActivity.insert(
                this,
                new Activity(
                        Activity.DEFAULT_ID,
                        editName.getText().toString(),
                        cat.getSelectedItem().toString(),
                        ums.getSelectedItem().toString(),
                        Integer.parseInt(editPrice.getText().toString())));
        hideVirtualKeyboard();
        reset();
        MDNNS.upNotificationInserted(v);
    }
}
