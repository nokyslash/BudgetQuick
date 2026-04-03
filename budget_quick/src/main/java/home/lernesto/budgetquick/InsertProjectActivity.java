package home.lernesto.budgetquick;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import home.lernesto.budgetquick.db.DataProjects;
import home.lernesto.budgetquick.pojos.ClientsPro;
import home.lernesto.budgetquick.pojos.Project;
import home.lernesto.budgetquick.utilsns.MDNNS;


public class InsertProjectActivity extends AppCompatActivity {
//    Visual Variables
    private Toolbar toolbar;
    private EditText editNamePro,editCost,editNameClient,editAddress;
    private CheckBox chFix;
    private Button insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_project);

//        To link file XML
        toolbar        = findViewById(R.id.appbar_insert_pro);
        editNamePro    = findViewById(R.id.edit_name_pro);
        chFix          = findViewById(R.id.chec_box_fix);
        editCost       = findViewById(R.id.edit_cost);
        editNameClient = findViewById(R.id.edit_client_name);
        editAddress    = findViewById(R.id.edit_address);
        insert         = findViewById(R.id.button_ok_insert_pro);

        initializeToolbar();
        initializeButton();

        chFix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)editCost.setVisibility(View.VISIBLE);
                else editCost.setVisibility(View.INVISIBLE);
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chFix.isChecked()){
                    if (!editNamePro.getText().toString().isEmpty() && !editCost.getText().toString().isEmpty() &&
                            !editNameClient.getText().toString().isEmpty() && !editAddress.getText().toString().isEmpty())
                        insertWithFixCost(v);
                    else MDNNS.upNotificationEmptyField(v);
                } else if (!editNamePro.getText().toString().isEmpty() && !editNameClient.getText().toString().isEmpty() &&
                        !editAddress.getText().toString().isEmpty())
                            insertWithVariableCost(v);
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
            toolbar.setTitle(R.string.new_pro);
            setSupportActionBar(toolbar);
        }
    }

    private void initializeButton(){
        insert.setText(R.string.insert);
    }

    private void hideVirtualKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        assert imm != null;
        imm.hideSoftInputFromWindow(editNamePro.getWindowToken(),0);
        imm.hideSoftInputFromWindow(editCost.getWindowToken(),0);
        imm.hideSoftInputFromWindow(editNameClient.getWindowToken(),0);
        imm.hideSoftInputFromWindow(editAddress.getWindowToken(), 0);
    }

    private void reset(){
        YoYo.with(Techniques.Shake).playOn(findViewById(R.id.edit_name_pro));
        YoYo.with(Techniques.Shake).playOn(findViewById(R.id.edit_client_name));
        YoYo.with(Techniques.Shake).playOn(findViewById(R.id.edit_address));
        chFix.setChecked(false);
        editNamePro.setText("");
        editCost.setText("");
        editNameClient.setText("");
        editAddress.setText("");
    }

    private void insertWithFixCost(View v){
        hideVirtualKeyboard();
        DataProjects.insert(
                this,
                new Project(
                        Project.TEMP_ID,
                        Integer.parseInt(editCost.getText().toString()),
                        Project.FIX_COST,
                        editNamePro.getText().toString(), Project.DEFAULT_LOCK),
                new ClientsPro(
                        editNameClient.getText().toString(),
                        editAddress.getText().toString()));

        reset();
        MDNNS.upNotificationInserted(v);
    }

    private void insertWithVariableCost(View v){
        hideVirtualKeyboard();
        DataProjects.insert(
                this,
                new Project(
                        Project.TEMP_ID,
                        Project.INITIAL_COST,
                        Project.VARIABLE_COST,
                        editNamePro.getText().toString(), Project.DEFAULT_LOCK),
                new ClientsPro(
                        editNameClient.getText().toString(),
                        editAddress.getText().toString()));

        reset();
        MDNNS.upNotificationInserted(v);
    }
}
