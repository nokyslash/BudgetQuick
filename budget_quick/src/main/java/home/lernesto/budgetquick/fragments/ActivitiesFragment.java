package home.lernesto.budgetquick.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scalified.fab.ActionButton;
import com.scalified.uitools.convert.DensityConverter;

import java.util.Objects;

import home.lernesto.budgetquick.InsertActActivity;
import home.lernesto.budgetquick.R;
import home.lernesto.budgetquick.adapters.ActivityAdapter;
import home.lernesto.budgetquick.db.DataActivity;


public class ActivitiesFragment extends Fragment {
    private Spinner cat;
    private RecyclerView act;
    private ActionButton fab;

    private float distance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_with_spn, container, false);

        cat = v.findViewById(R.id.spinner_cat);
        act = v.findViewById(R.id.rcview_act);
        fab = v.findViewById(R.id.fab_act);

        builDistance();
        initializeSpinner();
        initializeRecylerView();

        cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), InsertActActivity.class));
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

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshRecyclerView();
        fab.show();
    }

    @Override
    public void onPause() {
        fab.setVisibility(View.INVISIBLE);
        super.onPause();
    }

    private void initializeRecylerView(){
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        act.setLayoutManager(llm);
        act.setAdapter(new ActivityAdapter(DataActivity.getActivities(getContext(), cat.getSelectedItem().toString()), getActivity()));
    }

    private void refreshRecyclerView(){
        ((ActivityAdapter) Objects.requireNonNull(act.getAdapter())).refreshData(cat.getSelectedItem().toString());
        act.getAdapter().notifyDataSetChanged();
    }

    private void initializeSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                Objects.requireNonNull(getContext()),
                R.array.categories_total,
                R.layout.support_simple_spinner_dropdown_item);

        cat.setAdapter(adapter);
    }

    private void changeSignDistance(){
        if (distance < 0)distance = Math.abs(distance);
        else distance = -(Math.abs(distance));
    }

    private void builDistance(){
        this.distance = getResources().getConfiguration().screenWidthDp
                - DensityConverter.pxToDp(
                Objects.requireNonNull(getActivity()),
                fab.getSize())
                - DensityConverter.pxToDp(
                getActivity(),
                getResources().getDimension(R.dimen.activity_horizontal_margin)) * 4;
    }
}
