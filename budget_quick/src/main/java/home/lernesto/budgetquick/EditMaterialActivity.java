package home.lernesto.budgetquick;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import home.lernesto.budgetquick.db.DataMaterials;
import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.pojos.Material;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class EditMaterialActivity extends AppCompatActivity {
//    Visual Variables
    private Toolbar toolbar;
    private EditText editName, editFormat;
    private Button btnEdit;

//    Logical Variables
    private int idM;
    private String nameM, format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_material);

//        To link file XML
        toolbar    = findViewById(R.id.appbar_insert_mat);
        editName   = findViewById(R.id.edit_name_mat);
        editFormat = findViewById(R.id.edit_format);
        btnEdit    = findViewById(R.id.button_ok_insert_mat);

//        Extract Extras
        idM    = getIntent().getIntExtra(ConstDB.COLUMN_IDM, 0);
        nameM  = getIntent().getStringExtra(ConstDB.COLUMN_NAMEM);
        format = getIntent().getStringExtra(ConstDB.COLUMN_FORMAT);

        initializeToolbar();
        initializeEditText();
        initializeButton();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editName.getText().toString().isEmpty() && !editFormat.getText().toString().isEmpty()) edit(v);
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
            toolbar.setTitle(R.string.edit_mat);
            setSupportActionBar(toolbar);
        }
    }

    private void initializeEditText(){
        editName.setText(nameM);
        editFormat.setText(format);
    }

    private void initializeButton(){
        btnEdit.setText(R.string.edit);
    }

    private void hideVirtualKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        assert imm != null;
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editFormat.getWindowToken(), 0);
    }

    private void edit(View v){
        DataMaterials.edit(
                this,
                new Material(idM, editName.getText().toString(), editFormat.getText().toString()));
        hideVirtualKeyboard();
        MDNNS.upNotificationEdited(this, v);
    }
}
