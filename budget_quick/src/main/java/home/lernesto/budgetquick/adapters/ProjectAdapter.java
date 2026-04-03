package home.lernesto.budgetquick.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andexert.library.RippleView;
import com.youngtr.numberprogressbar.NumberProgressBar;

import java.util.ArrayList;

import home.lernesto.budgetquick.CostActActivity;
import home.lernesto.budgetquick.R;
import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.db.DataCostAct;
import home.lernesto.budgetquick.db.DataProjects;
import home.lernesto.budgetquick.pojos.Project;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
    private ArrayList<Project> data;
    private Activity activity;

    /*private ArrayList<Project> selectedPro;
    boolean enabled;
    boolean selectedAll;*/

    public ProjectAdapter(ArrayList<Project> projects, Activity activity) {
        this.data = projects;
        this.activity = activity;
        /*this.selectedPro = new ArrayList<>();
        enabled = false;
        selectedAll = false;*/
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_project,parent,false);
        return new ProjectViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ProjectViewHolder holder, final int position) {
        if (data.get(position).getCostType().equals(Project.FIX_COST)) holder.typeC.setText(R.string.fix_cost_for_cardview);
        else holder.typeC.setText(R.string.variable_cost_for_cardview);

        if (data.get(position).isLock())holder.ivLock.setImageResource(R.drawable.ic_locked);
        else holder.ivLock.setImageResource(R.drawable.ic_unlocked);

        holder.title.setText(data.get(position).getNameProject());

//        True condition: Project not finished and unlock and variable cost
        if (!DataProjects.isFinished(activity, data.get(position).getIdProject()) &&
                !data.get(position).isLock() && data.get(position).getCostType().equals(Project.VARIABLE_COST)){

            int total = DataCostAct.calcTCost(activity,data.get(position).getIdProject());
            DataProjects.editTotalCost(activity,data.get(position).getIdProject(), total);
            holder.subtitle.setText("$" + total);
        } else holder.subtitle.setText("$" + data.get(position).getTotalCost());

        if (DataCostAct.countAct(activity, data.get(position).getIdProject()) > 0){
            holder.nProgress.setProgress(DataCostAct.getProgressProInPercent(activity, data.get(position).getIdProject()));
            holder.nProgress.setVisibility(View.VISIBLE);
        } else holder.nProgress.setVisibility(View.GONE);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ripple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        Intent intent = new Intent(activity, CostActActivity.class);
                        intent.putExtra(ConstDB.COLUMN_IDP,data.get(position).getIdProject());
                        intent.putExtra(ConstDB.COLUMN_NAMEP, data.get(position).getNameProject());
                        intent.putExtra(ConstDB.COLUMN_LOCK, data.get(position).isLock());

                        if (data.get(position).getCostType().equals(Project.FIX_COST)) {
                            intent.putExtra(ConstDB.COLUMN_COST_TYPE, true);
                            intent.putExtra(ConstDB.COLUMN_TCOST, data.get(position).getTotalCost());
                        }
                        else intent.putExtra(ConstDB.COLUMN_COST_TYPE, false);

                        activity.startActivity(intent);
                    }
                });
            }
        });

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                holder.ripple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        MDNNS.upPopupMenuPro(
                                v,
                                activity,
                                data.get(position),
                                DataProjects.getDataClient(
                                        activity,
                                        data.get(position).getIdProject()));
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

    public void refreshData(){data = DataProjects.getData(activity);}

    static class ProjectViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout layout;
//        private CheckBox chbSelectedForDelete;
        private TextView typeC, title, subtitle;
        private NumberProgressBar nProgress;
        private ImageView ivLock;
        private RippleView ripple;

        ProjectViewHolder(View itemView) {
            super(itemView);

//            chbSelectedForDelete = itemView.findViewById(R.id.chb_selected_for_delete);
            typeC                = itemView.findViewById(R.id.txview_typeCost);
            title                = itemView.findViewById(R.id.txview_title);
            subtitle             = itemView.findViewById(R.id.txview_subtitle);
            nProgress            = itemView.findViewById(R.id.progress_horizontal_pro);
            layout               = itemView.findViewById(R.id.linear_layout_background);
            ivLock               = itemView.findViewById(R.id.imageViewLock);
            ripple               = itemView.findViewById(R.id.rppv_pro_backg);
        }
    }
}
