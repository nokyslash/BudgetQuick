package home.lernesto.budgetquick;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.Objects;

import home.lernesto.budgetquick.adapters.NotesAdapter;
import home.lernesto.budgetquick.db.DataNotes;
import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.pojos.Note;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class NotesActivity extends AppCompatActivity {
//    Constants
    public static final String INSERT = "insert";
    public static final String EDIT = "edit";
    public static final String FLAG = "FLAG";

//    Visual Variables
    private Toolbar toolbar;
    private RecyclerView rcvNotes;
    private EditText edtBody;

    //    Logical Variables
    private int idP, selectedIdN;
    private String nameP, flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

//        To Link fle XML
        toolbar             = findViewById(R.id.app_bar_notes);
        rcvNotes            = findViewById(R.id.rcv_notes);
        edtBody             = findViewById(R.id.edt_body);
        ImageButton imgbAdd = findViewById(R.id.imgb_add);

//        Extract Extras
        idP   = getIntent().getIntExtra(ConstDB.COLUMN_IDP, 0);
        nameP = getIntent().getStringExtra(ConstDB.COLUMN_NAMEP);
        flag  = INSERT;

        initializeToolbar();
        initializeRecyclerView();

        imgbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtBody.getText().toString().isEmpty()) {
                    switch (flag){
                        case INSERT:
                            insert();
                            break;
                        case EDIT:
                            edit();
                            flag = INSERT;
                            break;
                    }
                } else MDNNS.upNotificationEmptyField(v);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (flag.equals(EDIT))flag = INSERT;
        else finish();
    }

    private void initializeToolbar(){
        if (toolbar != null){
            toolbar.setTitle(getString(R.string.notes_of) + " " + nameP);
            setSupportActionBar(toolbar);
        }
    }

    private void initializeRecyclerView(){
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcvNotes.setLayoutManager(llm);
        rcvNotes.setAdapter(new NotesAdapter(DataNotes.getData(this, idP), this));
    }

    public void refreshRecyclerView(){
        ((NotesAdapter) Objects.requireNonNull(rcvNotes.getAdapter())).refreshData(idP);
        rcvNotes.getAdapter().notifyDataSetChanged();
    }

    private void hideVirtualKeyBoard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        assert imm != null;
        imm.hideSoftInputFromWindow(edtBody.getWindowToken(), 0);
    }

    public void reset(){
        edtBody.setText("");
    }

    private void insert(){
        hideVirtualKeyBoard();
        DataNotes.insert(this, new Note(idP, Note.DEFAULT_ID, new Date(), edtBody.getText().toString()));
        reset();
        refreshRecyclerView();
    }

    private void edit(){
        hideVirtualKeyBoard();
        DataNotes.edit(this, new Note(idP, selectedIdN, null, edtBody.getText().toString()));
        reset();
        refreshRecyclerView();
    }

    public void setTextForToEdit(String textForToEdit){
        edtBody.setText(textForToEdit);
    }

    public void setFlag(String flag) { this.flag = flag;}

    public void setSelectedIdN(int selectedIdN) { this.selectedIdN = selectedIdN; }
}
