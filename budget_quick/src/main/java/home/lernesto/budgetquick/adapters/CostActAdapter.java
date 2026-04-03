package home.lernesto.budgetquick.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andexert.library.RippleView;
import com.youngtr.numberprogressbar.NumberProgressBar;

import java.util.ArrayList;

import home.lernesto.budgetquick.R;
import home.lernesto.budgetquick.db.DataCostAct;
import home.lernesto.budgetquick.pojos.CostActExtended;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class CostActAdapter extends RecyclerView.Adapter<CostActAdapter.CostActViewHolder>{
    private ArrayList<CostActExtended> data;
    private final Activity activity;

    public CostActAdapter(ArrayList<CostActExtended> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CostActViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_cost_act,parent,false);
        return new CostActViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final CostActViewHolder holder, final int position) {

        holder.title.setText(data.get(position).getNameAct());
        holder.subtitle.setText(prepareSubtitle(position));
        if (!data.get(position).getCostAct().isDual()) holder.dual.setVisibility(View.INVISIBLE);
        else holder.dual.setVisibility(View.VISIBLE);

        holder.progressBar.setProgress(
                DataCostAct.getProgressActInPercent(
                        activity, data.get(position).getCostAct().getIdPro(), data.get(position).getCostAct().getIdAct()));

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                holder.ripple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        MDNNS.upPopupMenuCostAct(v, activity, data.get(position));
                    }
                });
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class CostActViewHolder extends RecyclerView.ViewHolder{
        private final RelativeLayout layout;
        private final TextView dual;
        private final TextView title;
        private final TextView subtitle;
        private final NumberProgressBar progressBar;
        private final RippleView ripple;

        CostActViewHolder(View itemView){
            super(itemView);
            layout      = itemView.findViewById(R.id.relative_layout_bacground_cost_act);
            dual        = itemView.findViewById(R.id.txview_dual);
            title       = itemView.findViewById(R.id.txview_title_cost_act);
            subtitle    = itemView.findViewById(R.id.txview_size);
            progressBar = itemView.findViewById(R.id.progress_horizontal_cost_act);
            ripple      = itemView.findViewById(R.id.rppv_cost_backg);
        }
    }

    private String prepareSubtitle(int pos){
        return data.get(pos).getCostAct().getSize() + " " + data.get(pos).getUm();
    }

    public void refreshData(int idPro, String room){data = DataCostAct.getDataByRoom(activity, idPro, room);
    }
}
