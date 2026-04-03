package home.lernesto.budgetquick.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andexert.library.RippleView;

import java.util.ArrayList;

import home.lernesto.budgetquick.R;
import home.lernesto.budgetquick.db.DataMaterials;
import home.lernesto.budgetquick.pojos.Material;
import home.lernesto.budgetquick.utilsns.MDNNS;


public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>{
    private ArrayList<Material> data;
    private final Activity activity;

    public MaterialAdapter(ArrayList<Material> list, Activity activity) {
        this.data = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_title_subtitle,parent,false);
        return new MaterialViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MaterialViewHolder holder, final int position) {
        holder.title.setText(data.get(position).getNameMaterial());
        holder.subtitle.setText(data.get(position).getFormat());

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                holder.ripple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        MDNNS.upPopupMenuMat(v, activity, data.get(position));
                    }
                });
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void refreshData(){data = DataMaterials.getData(activity);}

    static class MaterialViewHolder extends RecyclerView.ViewHolder{
        private final LinearLayout layout;
        private final TextView title;
        private final TextView subtitle;
        private final RippleView ripple;

        MaterialViewHolder(View itemView) {
            super(itemView);
            layout   = itemView.findViewById(R.id.linear_layout_background);
            title    = itemView.findViewById(R.id.txview_title);
            subtitle = itemView.findViewById(R.id.txview_subtitle);
            ripple   = itemView.findViewById(R.id.rppv_effect_backg);
        }
    }
}
