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
import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.db.DataExpenseMat;
import home.lernesto.budgetquick.pojos.ExpenseMatExtended;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class ExpenseMatAdapter extends RecyclerView.Adapter<ExpenseMatAdapter.ExpenseMatViewHolder>{
    private ArrayList<ExpenseMatExtended> data;
    private final Activity activity;

    public ExpenseMatAdapter(ArrayList<ExpenseMatExtended> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ExpenseMatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_title_subtitle,parent,false);
        return new ExpenseMatViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ExpenseMatViewHolder holder, final int position) {
        holder.title.setText(data.get(position).getNameMat());
        holder.subtitle.setText(prepareSubtitle(position));

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                holder.ripple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        MDNNS.upPopupMenuExpenseMat(v, activity, data.get(position));
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

    static class ExpenseMatViewHolder extends RecyclerView.ViewHolder{
        private final LinearLayout layout;
        private final TextView title;
        private final TextView subtitle;
        private final RippleView ripple;

        ExpenseMatViewHolder(View itemView) {
            super(itemView);
            layout   = itemView.findViewById(R.id.linear_layout_background);
            title    = itemView.findViewById(R.id.txview_title);
            subtitle = itemView.findViewById(R.id.txview_subtitle);
            ripple   = itemView.findViewById(R.id.rppv_effect_backg);
        }
    }

    public void refreshData(int idAct){data = DataExpenseMat.getData(activity, idAct); }

    private String prepareSubtitle(int pos){
        return data.get(pos).getExpenseMat().getAmount() + " " +
                data.get(pos).getFormat() + " x " +
                activity.getIntent().getStringExtra(ConstDB.COLUMN_UNIT);
    }
}
