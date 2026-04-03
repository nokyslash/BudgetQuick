package home.lernesto.budgetquick.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scalified.fab.ActionButton;
import com.scalified.uitools.convert.DensityConverter;

import java.util.Objects;

import home.lernesto.budgetquick.InsertMaterialActivity;
import home.lernesto.budgetquick.R;
import home.lernesto.budgetquick.adapters.MaterialAdapter;
import home.lernesto.budgetquick.db.DataMaterials;


public class MaterialsFragment extends Fragment {
    private RecyclerView materials;
    private ActionButton fab;

    private float distance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        materials = v.findViewById(R.id.rcview_list_f);
        fab       = v.findViewById(R.id.fab_add_f);

        builDistance();
        initializeRecylerView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), InsertMaterialActivity.class));
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
        materials.setLayoutManager(llm);
        materials.setAdapter(new MaterialAdapter(DataMaterials.getData(getContext()), getActivity()));
    }

    private void refreshRecyclerView(){
        ((MaterialAdapter) Objects.requireNonNull(materials.getAdapter())).refreshData();
        materials.getAdapter().notifyDataSetChanged();
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
