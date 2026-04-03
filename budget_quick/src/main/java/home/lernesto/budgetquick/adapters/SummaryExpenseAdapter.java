package home.lernesto.budgetquick.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import home.lernesto.budgetquick.R;


public class SummaryExpenseAdapter extends RecyclerView.Adapter<SummaryExpenseAdapter.SumamryViewHolder>{
    private final String[] data;

    public SummaryExpenseAdapter(String[] data) {
        this.data = data;
    }

    @NonNull
    @Override
    public SumamryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SumamryViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.one_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(SumamryViewHolder holder, int position) {
        holder.item.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    static class SumamryViewHolder extends RecyclerView.ViewHolder{
        private final TextView item;

        SumamryViewHolder(View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.txv_item);
        }
    }
}
