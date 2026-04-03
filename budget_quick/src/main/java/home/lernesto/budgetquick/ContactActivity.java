package home.lernesto.budgetquick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scalified.fab.ActionButton;
import com.scalified.uitools.convert.DensityConverter;

import java.util.Objects;

import home.lernesto.budgetquick.adapters.ContactAdapter;
import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.db.DataContact;

public class ContactActivity extends AppCompatActivity {
//    Visual Variables
    private Toolbar toolbar;
    private RecyclerView rcvContacts;
    private ActionButton fab;

    //    Logical Variables
    private float distance;
    private int idP;
    private String nameP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_with_fab);

//        To link file XML
        toolbar     = findViewById(R.id.appbar_list_fab);
        rcvContacts = findViewById(R.id.rcview_list_a);
        fab         = findViewById(R.id.fab_add_a);

//        Extract Extras
        idP   = getIntent().getIntExtra(ConstDB.COLUMN_IDP, 0);
        nameP = getIntent().getStringExtra(ConstDB.COLUMN_NAMEP);

        builDistance();
        initializeToolbar();
        initializeRecyclerView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, InsertContactActivity.class);
                intent.putExtra(ConstDB.COLUMN_IDP, idP);
                intent.putExtra(ConstDB.COLUMN_NAMEP, nameP);

                startActivity(intent);
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                fab.moveLeft(distance);
                changeSignDistance();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshRecyclerView();
        fab.show();
    }

    @Override
    public void onPause() {
        fab.setVisibility(View.INVISIBLE);
        super.onPause();
    }

    private void initializeToolbar(){
        if (toolbar != null){
            toolbar.setTitle(getString(R.string.contacts_of) + " " + nameP);
            setSupportActionBar(toolbar);
        }
    }

    private void initializeRecyclerView(){
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcvContacts.setLayoutManager(llm);
        rcvContacts.setAdapter(new ContactAdapter(DataContact.getData(this, idP), this));
    }

    private void refreshRecyclerView(){
        ((ContactAdapter) Objects.requireNonNull(rcvContacts.getAdapter())).refreshData(idP);
        rcvContacts.getAdapter().notifyDataSetChanged();
    }

    public void resumeActivity(){onResume();}

    private void builDistance(){
        this.distance = getResources().getConfiguration().screenWidthDp
                - DensityConverter.pxToDp(this, fab.getSize())
                - DensityConverter.pxToDp(this, getResources().getDimension(R.dimen.activity_horizontal_margin)) * 4;
    }

    private void changeSignDistance(){
        if (distance < 0)distance = Math.abs(distance);
        else distance = -(Math.abs(distance));
    }
}
