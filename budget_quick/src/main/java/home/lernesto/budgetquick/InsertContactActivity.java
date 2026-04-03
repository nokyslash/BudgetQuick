package home.lernesto.budgetquick;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import home.lernesto.budgetquick.db.DataContact;
import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.pojos.Contact;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class InsertContactActivity extends AppCompatActivity {
    //    Visual Variables
    private Toolbar toolbar;
    private EditText edtName, edtPhone;
    private Button btnInsert;

//    Logical Variables
    private Context context;
    private int idP;
    private String nameP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_material);

//        To link file XML
        toolbar   = findViewById(R.id.appbar_insert_mat);
        edtName   = findViewById(R.id.edit_name_mat);
        edtPhone  = findViewById(R.id.edit_format);
        btnInsert = findViewById(R.id.button_ok_insert_mat);

//        Extract Extras
        idP   = getIntent().getIntExtra(ConstDB.COLUMN_IDP, 0);
        nameP = getIntent().getStringExtra(ConstDB.COLUMN_NAMEP);

        context = this;

        initializeToolbar();
        initializeEditTexts();
        initializeButton();

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtName.getText().toString().isEmpty() && !edtPhone.getText().toString().isEmpty())
                    if (DataContact.noExsistContact(context, idP, edtPhone.getText().toString()))insert(v);
                    else MDNNS.upNotificationAlreadyExist(v);
                else MDNNS.upNotificationEmptyField(v);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initializeToolbar(){
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.insert_contact) + " " + nameP);
            setSupportActionBar(toolbar);
        }
    }

    private void initializeEditTexts(){
        edtName.setHint(R.string.contact_name);

        edtPhone.setHint(R.string.phone);
        edtPhone.setInputType(InputType.TYPE_CLASS_PHONE);
    }

    private void initializeButton(){
        btnInsert.setText(R.string.insert);
    }

    private void hideVirtualKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        assert imm != null;
        imm.hideSoftInputFromWindow(edtName.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(edtPhone.getWindowToken(), 0);
    }

    private void reset(){
        YoYo.with(Techniques.Shake).playOn(findViewById(R.id.edit_name_mat));
        YoYo.with(Techniques.Shake).playOn(findViewById(R.id.edit_format));
        edtName.setText("");
        edtPhone.setText("");
    }

    private void insert(View view){
        DataContact.insert(this, new Contact(idP, edtPhone.getText().toString(), edtName.getText().toString()));
        hideVirtualKeyboard();
        reset();
        MDNNS.upNotificationInserted(view);
    }
}
