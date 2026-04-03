package home.lernesto.budgetquick;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import home.lernesto.budgetquick.db.DataMaterials;
import home.lernesto.budgetquick.pojos.Material;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class InsertMaterialActivity extends AppCompatActivity {
//    Visual Variables
    private Toolbar toolbar;
    private EditText editName, editFormat;
    private Button btnInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_material);

//        To link file XML
        toolbar    = findViewById(R.id.appbar_insert_mat);
        editName   = findViewById(R.id.edit_name_mat);
        editFormat = findViewById(R.id.edit_format);
        btnInsert  = findViewById(R.id.button_ok_insert_mat);

        initializeToolbar();
        initializeButton();

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editName.getText().toString().isEmpty() && !editFormat.getText().toString().isEmpty())insert(v);
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
            toolbar.setTitle(R.string.new_mat);
            setSupportActionBar(toolbar);
        }
    }

    private void initializeButton(){
        btnInsert.setText(R.string.insert);
    }

    private void hideVirtualKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        assert imm != null;
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editFormat.getWindowToken(), 0);
    }

    private void reset(){
        YoYo.with(Techniques.Shake).playOn(findViewById(R.id.edit_name_mat));
        YoYo.with(Techniques.Shake).playOn(findViewById(R.id.edit_format));
        editName.setText("");
        editFormat.setText("");
    }

    private void insert(View v){
        hideVirtualKeyboard();
        DataMaterials.insert(
                this,
                new Material(
                        Material.DEFAULT_ID,
                        editName.getText().toString(),
                        editFormat.getText().toString()));
        reset();
        MDNNS.upNotificationInserted(v);
    }
}
