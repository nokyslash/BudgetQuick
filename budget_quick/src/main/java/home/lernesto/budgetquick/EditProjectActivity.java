package home.lernesto.budgetquick;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.db.DataCostAct;
import home.lernesto.budgetquick.db.DataProjects;
import home.lernesto.budgetquick.pojos.ClientsPro;
import home.lernesto.budgetquick.pojos.Project;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class EditProjectActivity extends AppCompatActivity {
//    Visual Variables
    private Activity activity;
    private Context context;
    private Toolbar toolbar;
    private CheckBox chFix;
    private EditText editNamePro,editCost,editNameClient,editAddress;
    private Button btnEdit;

//    Logical Variables
    private int idP, tCost;
    private String nameP, nameCli, address;
    private boolean fixCost;
    private boolean lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_project);

//        To link file XML
        activity       = this;
        context        = this;
        toolbar        = findViewById(R.id.appbar_insert_pro);
        chFix          = findViewById(R.id.chec_box_fix);
        editNamePro    = findViewById(R.id.edit_name_pro);
        editCost       = findViewById(R.id.edit_cost);
        editNameClient = findViewById(R.id.edit_client_name);
        editAddress    = findViewById(R.id.edit_address);
        btnEdit        = findViewById(R.id.button_ok_insert_pro);

//        Extract Extras
        idP     = getIntent().getIntExtra(ConstDB.COLUMN_IDP, 0);
        tCost   = getIntent().getIntExtra(ConstDB.COLUMN_TCOST, 0);
        nameP   = getIntent().getStringExtra(ConstDB.COLUMN_NAMEP);
        nameCli = getIntent().getStringExtra(ConstDB.COLUMN_CLIENT_NAME);
        address = getIntent().getStringExtra(ConstDB.COLUMN_ADDRESS);
        fixCost = getIntent().getBooleanExtra(ConstDB.COLUMN_COST_TYPE, false);
        lock    = getIntent().getBooleanExtra(ConstDB.COLUMN_LOCK, false);

        initializeToolbar();
        initiliazeCheckBox();
        initializeEditText();
        initializeButton();

        chFix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) editCost.setVisibility(View.VISIBLE);
                else editCost.setVisibility(View.INVISIBLE);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                When variable cost is changed to fix cost
                if (!fixCost && chFix.isChecked()){
                    if (!editNamePro.getText().toString().isEmpty() && !editCost.getText().toString().isEmpty() &&
                            !editNameClient.getText().toString().isEmpty() && !editAddress.getText().toString().isEmpty()){
                        editEndFixCost(v);
                        DataCostAct.toNotDual(context, idP);
                    } else MDNNS.upNotificationEmptyField(v);
                }

//                When varible cost is not changed
                if (!fixCost && !chFix.isChecked()){
                    if (!editNamePro.getText().toString().isEmpty() && !editNameClient.getText().toString().isEmpty() &&
                            !editAddress.getText().toString().isEmpty()){
                        DataProjects.edit(
                                context,
                                new Project(
                                        idP, tCost, Project.VARIABLE_COST, editNamePro.getText().toString(), lock),
                                new ClientsPro(
                                        editNameClient.getText().toString(), editAddress.getText().toString()));

                        hideVirtualKeyboard();
                        MDNNS.upNotificationEdited(activity, v);
                    } else MDNNS.upNotificationEmptyField(v);
                }

//               When fix cost is changed to variable cost
                if (fixCost && !chFix.isChecked()){
                    if (!editNamePro.getText().toString().isEmpty() && !editNameClient.getText().toString().isEmpty() &&
                            !editAddress.getText().toString().isEmpty()) {
                        DataProjects.edit(
                                context,
                                new Project(
                                        idP, Project.INITIAL_COST, Project.VARIABLE_COST, editNamePro.getText().toString(), lock),
                                new ClientsPro(
                                        editNameClient.getText().toString(), editAddress.getText().toString()));

                        DataCostAct.toDual(context, idP);
                        hideVirtualKeyboard();
                        MDNNS.upDialogReminder(activity);
                        MDNNS.upNotificationEdited(activity, v);

                    } else MDNNS.upNotificationEmptyField(v);
                }

//                When fix cost is not changed
                if (fixCost && chFix.isChecked()){
                    if (!editNamePro.getText().toString().isEmpty() && !editNameClient.getText().toString().isEmpty() &&
                            !editAddress.getText().toString().isEmpty()) editEndFixCost(v);
                    else MDNNS.upNotificationEmptyField(v);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initializeToolbar(){
        if (toolbar != null){
            toolbar.setTitle(R.string.edit_pro);
            setSupportActionBar(toolbar);
        }
    }

    private void initiliazeCheckBox(){
        chFix.setChecked(fixCost);
    }

    @SuppressLint("SetTextI18n")
    private void initializeEditText(){
        editNamePro.setText(nameP);
        editNameClient.setText(nameCli);
        editAddress.setText(address);

        if (chFix.isChecked()){
            editCost.setText(Integer.toString(tCost));
            editCost.setVisibility(View.VISIBLE);
        }
    }

    private void initializeButton(){
        btnEdit.setText(R.string.edit);
    }

    private void hideVirtualKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        assert imm != null;
        imm.hideSoftInputFromWindow(editNamePro.getWindowToken(),0);
        imm.hideSoftInputFromWindow(editCost.getWindowToken(),0);
        imm.hideSoftInputFromWindow(editNameClient.getWindowToken(),0);
        imm.hideSoftInputFromWindow(editAddress.getWindowToken(), 0);
    }

    private void editEndFixCost(View v){
        DataProjects.edit(
                this,
                new Project(
                        idP,
                        Integer.parseInt(editCost.getText().toString()),
                        Project.FIX_COST,
                        editNamePro.getText().toString(), lock),
                new ClientsPro(editNameClient.getText().toString(), editAddress.getText().toString()));

        hideVirtualKeyboard();
        MDNNS.upNotificationEdited(this, v);
    }
}
