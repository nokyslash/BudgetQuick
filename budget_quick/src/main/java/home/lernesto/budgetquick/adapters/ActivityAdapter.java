package home.lernesto.budgetquick.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andexert.library.RippleView;

import java.util.ArrayList;

import home.lernesto.budgetquick.ExpenseMatActivity;
import home.lernesto.budgetquick.R;
import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.db.DataActivity;
import home.lernesto.budgetquick.pojos.Activity;
import home.lernesto.budgetquick.utilsns.MDNNS;


public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {
    private ArrayList<Activity> data;
    private final android.app.Activity activity;

    public ActivityAdapter(ArrayList<Activity> list, android.app.Activity activity) {
        this.data = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(
                R.layout.cardview_activities,
                parent,
                false
        );
        return new ActivityViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ActivityViewHolder holder, final int position) {
        holder.title.setText(data.get(position).getNameAct());
        holder.price.setText("$" + preparePrice(position));
        holder.unit.setText(data.get(position).getUm());

        holder.layout.setOnClickListener(v -> holder.ripple.setOnRippleCompleteListener(
                rippleView -> {
                    Intent intent = new Intent(activity, ExpenseMatActivity.class);
                    intent.putExtra(ConstDB.COLUMN_IDA, data.get(position).getIdActivity());
                    intent.putExtra(ConstDB.COLUMN_NAMEA, data.get(position).getNameAct());
                    intent.putExtra(ConstDB.COLUMN_UNIT, data.get(position).getUm());
                    activity.startActivity(intent);
                }));

        holder.layout.setOnLongClickListener(v -> {
            holder.ripple.setOnRippleCompleteListener(rippleView -> MDNNS.upPopupMenuAct(
                    v,
                    activity,
                    data.get(position)
                    )
            );
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout layout;
        private final TextView title;
        private final TextView price;
        private final TextView unit;
        private final RippleView ripple;

        ActivityViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.relative_layout_background_act);
            title = itemView.findViewById(R.id.txview_title_act);
            price = itemView.findViewById(R.id.txview_price_act);
            unit = itemView.findViewById(R.id.txview_um_act);
            ripple = itemView.findViewById(R.id.rppv_act_backg);

        }
    }

    private String preparePrice(int pos) {
        return Integer.toString(data.get(pos).getPrice());
    }

    public void refreshData(String cat) {
        data = DataActivity.getActivities(activity, cat);
    }
}
