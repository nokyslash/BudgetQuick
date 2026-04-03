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

import home.lernesto.budgetquick.adapters.ExpenseMatAdapter;
import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.db.DataExpenseMat;

public class ExpenseMatActivity extends AppCompatActivity {
//    Visual Variables
    private Toolbar toolbar;
    private RecyclerView list;
    private ActionButton fab;

//    Logical Variables
    private float distance;
    private int idA;
    private String nameA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_with_fab);

//        To link file XML
        toolbar = findViewById(R.id.appbar_list_fab);
        list    = findViewById(R.id.rcview_list_a);
        fab     = findViewById(R.id.fab_add_a);

//        Extract Extras
        idA   = getIntent().getIntExtra(ConstDB.COLUMN_IDA, 0);
        nameA = getIntent().getStringExtra(ConstDB.COLUMN_NAMEA);

        builDistance();
        initialzeToolbar();
        initializeRecyclerView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseMatActivity.this, InsertExpenseMatActivity.class);
                intent.putExtra(ConstDB.COLUMN_IDA, idA);
                intent.putExtra(ConstDB.COLUMN_NAMEA, nameA);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initialzeToolbar(){
        if (toolbar != null){
            toolbar.setTitle(getString(R.string.expense_on) + " " + nameA);
            setSupportActionBar(toolbar);
        }
    }

    private void initializeRecyclerView(){
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(llm);
        list.setAdapter(new ExpenseMatAdapter(DataExpenseMat.getData(this, idA), this));
    }

    private void refreshRecyclerView(){
        ((ExpenseMatAdapter) Objects.requireNonNull(list.getAdapter())).refreshData(idA);
        list.getAdapter().notifyDataSetChanged();
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
