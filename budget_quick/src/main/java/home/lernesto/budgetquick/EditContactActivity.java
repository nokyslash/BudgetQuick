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

import home.lernesto.budgetquick.db.DataContact;
import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.pojos.Contact;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class EditContactActivity extends AppCompatActivity {
    //    Visual Variables
    private Toolbar toolbar;
    private EditText edtName, edtPhone;
    private Button btnEdit;

    //    Logical Variables
    private Context context;
    private int idP;
    private String nameP, nameC, tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_material);

        //        To link file XML
        toolbar  = findViewById(R.id.appbar_insert_mat);
        edtName  = findViewById(R.id.edit_name_mat);
        edtPhone = findViewById(R.id.edit_format);
        btnEdit  = findViewById(R.id.button_ok_insert_mat);

//        Extract Extras
        idP   = getIntent().getIntExtra(ConstDB.COLUMN_IDP, 0);
        nameP = getIntent().getStringExtra(ConstDB.COLUMN_NAMEP);
        nameC = getIntent().getStringExtra(ConstDB.COLUMN_CONTACT);
        tel   = getIntent().getStringExtra(ConstDB.COLUMN_TEL);

        context = this;

        initializeToolbar();
        initializeEditTexts();
        initializeButton();


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtName.getText().toString().isEmpty() && !edtPhone.getText().toString().isEmpty())
                    if (!edtPhone.getText().toString().equals(tel))
                        if (DataContact.noExsistContact(context, idP, edtPhone.getText().toString()))edit(v);
                        else MDNNS.upNotificationAlreadyExist(v);
                    else edit(v);
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
            toolbar.setTitle(getString(R.string.edit_contact) + " " + nameP);
            setSupportActionBar(toolbar);
        }
    }

    private void initializeEditTexts(){
        edtName.setHint(R.string.contact_name);
        edtName.setText(nameC);

        edtPhone.setHint(R.string.phone);
        edtPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        edtPhone.setText(tel);
    }

    private void initializeButton(){
        btnEdit.setText(R.string.edit);
    }

    private void hideVirtualKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        assert imm != null;
        imm.hideSoftInputFromWindow(edtName.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(edtPhone.getWindowToken(), 0);
    }

    private void edit(View view){
        DataContact.edit(this, new Contact(idP, edtPhone.getText().toString(), edtName.getText().toString()), tel);
        hideVirtualKeyboard();
        MDNNS.upNotificationEdited(this, view);
    }
}
