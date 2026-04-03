package home.lernesto.budgetquick.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andexert.library.RippleView;

import java.text.DateFormat;
import java.util.ArrayList;

import home.lernesto.budgetquick.R;
import home.lernesto.budgetquick.db.DataNotes;
import home.lernesto.budgetquick.pojos.Note;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder>{
    private ArrayList<Note> data;
    private final Activity activity;

    public NotesAdapter(ArrayList<Note> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_notes, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotesViewHolder holder, final int position) {
        holder.txvDate.setText(prepareDate(position));
        holder.txvBody.setText(data.get(position).getBody());

        holder.llBackground.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                holder.ripple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        MDNNS.upPopupMenuNote(v, activity, data.get(position));
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

    static class NotesViewHolder extends RecyclerView.ViewHolder{
        private final LinearLayout llBackground;
        private final TextView txvDate;
        private final TextView txvBody;
        private final RippleView ripple;

        NotesViewHolder(View view){
            super(view);
            llBackground = view.findViewById(R.id.ll_background);
            txvDate      = view.findViewById(R.id.txv_date);
            txvBody      = view.findViewById(R.id.txv_body);
            ripple       = view.findViewById(R.id.rppv_notes_backg);
        }
    }

    public void refreshData(int idP){data = DataNotes.getData(activity, idP); }

    private String prepareDate(int pos){ return DateFormat.getDateTimeInstance().format(data.get(pos).getDate());
    }
}
