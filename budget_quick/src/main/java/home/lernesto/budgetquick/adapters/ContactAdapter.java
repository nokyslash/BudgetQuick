package home.lernesto.budgetquick.adapters;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andexert.library.RippleView;

import java.util.ArrayList;

import home.lernesto.budgetquick.R;
import home.lernesto.budgetquick.db.DataContact;
import home.lernesto.budgetquick.pojos.Contact;
import home.lernesto.budgetquick.utilsns.CNS;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private ArrayList<Contact> data;
    private final Activity activity;

    public ContactAdapter(ArrayList<Contact> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, final int position) {
        holder.txvTitle.setText(data.get(position).getName());
        holder.txvSubtitle.setText(data.get(position).getPhone());

        holder.llhBackground.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                holder.ripple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        MDNNS.upPopupMenuContact(v, activity, data.get(position));
                    }
                });
                return true;
            }
        });

        holder.imgVCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(position);
            }
        });

        holder.imgVSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout llhBackground;
        private final TextView txvTitle;
        private final TextView txvSubtitle;
        private final ImageView imgVCall;
        private final ImageView imgVSendSMS;
        private final RippleView ripple;


        ContactViewHolder(View itemView) {
            super(itemView);
            llhBackground = itemView.findViewById(R.id.llh_background);
            txvTitle      = itemView.findViewById(R.id.txv_title_name);
            txvSubtitle   = itemView.findViewById(R.id.txv_subtitle_phone);
            imgVCall      = itemView.findViewById(R.id.imgv_call);
            imgVSendSMS   = itemView.findViewById(R.id.imgv_sms);
            ripple        = itemView.findViewById(R.id.rppv_cont_backg);
        }
    }

    public void refreshData(int idPro){data = DataContact.getData(activity, idPro);}

    private void call(int pos) {
        if (CNS.checkStatusCallPhonePermission(activity))
        activity.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + data.get(pos).getPhone())));
        else {
            CNS.requestCallPhonePermission(activity);
            call(pos);
        }
    }

    private void sendSMS(int pos){
        activity.startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + data.get(pos).getPhone())));
    }
}
