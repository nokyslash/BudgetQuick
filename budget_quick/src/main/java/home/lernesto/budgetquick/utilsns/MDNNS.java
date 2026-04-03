package home.lernesto.budgetquick.utilsns;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;

import home.lernesto.budgetquick.ContactActivity;
import home.lernesto.budgetquick.CostActActivity;
import home.lernesto.budgetquick.EditActActivity;
import home.lernesto.budgetquick.EditContactActivity;
import home.lernesto.budgetquick.EditCostActActivity;
import home.lernesto.budgetquick.EditExpenseMatActivity;
import home.lernesto.budgetquick.EditMaterialActivity;
import home.lernesto.budgetquick.EditProjectActivity;
import home.lernesto.budgetquick.ExpenseMatActivity;
import home.lernesto.budgetquick.MainActivity;
import home.lernesto.budgetquick.NotesActivity;
import home.lernesto.budgetquick.R;
import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.db.DataActivity;
import home.lernesto.budgetquick.db.DataContact;
import home.lernesto.budgetquick.db.DataCostAct;
import home.lernesto.budgetquick.db.DataExpenseMat;
import home.lernesto.budgetquick.db.DataMaterials;
import home.lernesto.budgetquick.db.DataNotes;
import home.lernesto.budgetquick.db.DataProjects;
import home.lernesto.budgetquick.pojos.ClientsPro;
import home.lernesto.budgetquick.pojos.Contact;
import home.lernesto.budgetquick.pojos.CostActExtended;
import home.lernesto.budgetquick.pojos.ExpenseMatExtended;
import home.lernesto.budgetquick.pojos.Material;
import home.lernesto.budgetquick.pojos.Note;
import home.lernesto.budgetquick.pojos.Project;


public class MDNNS {
    private static final String PROJECT = "pro";
    private static final String ACTIVITY = "act";
    private static final String MATERIAL = "mat";
    private static final String COST = "cost";
    private static final String EXPENSE = "expense";
    private static final String NOTE = "note";
    private static final String CONTACT = "contact";

    public static void upNotificationEmptyField(View v){
        Snackbar.make(v, R.string.empty_field, Snackbar.LENGTH_SHORT).show();
    }

    public static void upNotificationThereIsNotMat(View v){
        Snackbar.make(v, R.string.there_is_not_mat, Snackbar.LENGTH_SHORT).show();
    }

    public static void upNotificationEmptyFieldRoom(View v){
        Snackbar.make(v, R.string.empty_field_room, Snackbar.LENGTH_SHORT).show();
    }

    public static void upNotificationInserted(View v){
        Snackbar.make(v, R.string.inserted, Snackbar.LENGTH_SHORT).show();
    }

    public static void upNotificationEdited(final Activity activity, View v){
        Snackbar.make(v,R.string.edited,Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                }).show();
    }

    public static void upNotificationAlreadyExist(View v){
        Snackbar.make(v, R.string.exist, Snackbar.LENGTH_SHORT).show();
    }

    public static void upNotificationThereIsNotElements(View v){
        Snackbar.make(v,R.string.there_is_not_elements,Snackbar.LENGTH_SHORT).show();
    }

    public static void upNotificationInvalidTotal(View v){
        Snackbar.make(v, R.string.invalid_total,Snackbar.LENGTH_LONG).show();
    }

    public static void upNotificationConfirmBackExit(View v){
        Snackbar.make(v, R.string.confirm_back_exit, Snackbar.LENGTH_SHORT).show();
    }

    public static void upNotificationEmptyProject(View v){
        Snackbar.make(v, R.string.empty_project, Snackbar.LENGTH_SHORT).show();
    }

    public static void upNotificationSuccesfullyExporting(Context context, View v, String path){
        String msg = context.getString(R.string.succesfully_exporting) + " " + path;
        Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show();
    }

    public static void upNotificationFailCreateFile(View v){
        Snackbar.make(v, R.string.fail_create_txt_file, Snackbar.LENGTH_SHORT).show();
    }

    public static void upNotificationInvalidDir(View v){
        Snackbar.make(v, R.string.invalid_dir, Snackbar.LENGTH_SHORT).show();
    }

    public static void upNotificationSecurityFail(View v){
        Snackbar.make(v, R.string.security_fail, Snackbar.LENGTH_SHORT).show();
    }

    public static void upNotificationInvalidDBFile(View v){
        Snackbar.make(v, R.string.invalid_db_file, Snackbar.LENGTH_LONG).show();
    }

    public static void upNotificationInvalidBudgetQuickDB(View v){
        Snackbar.make(v, R.string.invalid_budget_quick_db, Snackbar.LENGTH_LONG).show();
    }
    public static void upNotificationIOError(View v){
        Snackbar.make(v, R.string.io_error, Snackbar.LENGTH_LONG).show();
    }
    public static void upNotificationFileNotFound(View v){
        Snackbar.make(v, R.string.file_not_found, Snackbar.LENGTH_LONG).show();
    }

    public static void upPopupMenuPro(View v, final Activity activity, final Project project, final ClientsPro clientsPro){
        PopupMenu menu = new PopupMenu(v.getContext(), v);
        menu.getMenuInflater().inflate(R.menu.popup_menu_pro,menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.op_see_pro:
                        intent = new Intent(activity, CostActActivity.class);
                        intent.putExtra(ConstDB.COLUMN_IDP, project.getIdProject());
                        intent.putExtra(ConstDB.COLUMN_NAMEP, project.getNameProject());
                        intent.putExtra(ConstDB.COLUMN_LOCK, project.isLock());

                        intent.putExtra(ConstDB.COLUMN_COST_TYPE, project.getCostType().equals(Project.FIX_COST));

                        activity.startActivity(intent);
                        return true;

                    case R.id.op_client_pro:
                        upDialogClientInfo(activity, clientsPro);
                        return true;

                    case R.id.op_edit_pro:
                        intent = new Intent(activity, EditProjectActivity.class);
                        intent.putExtra(ConstDB.COLUMN_IDP, project.getIdProject());
                        intent.putExtra(ConstDB.COLUMN_TCOST, project.getTotalCost());
                        intent.putExtra(ConstDB.COLUMN_NAMEP, project.getNameProject());
                        intent.putExtra(ConstDB.COLUMN_CLIENT_NAME, clientsPro.getName());
                        intent.putExtra(ConstDB.COLUMN_ADDRESS, clientsPro.getClientAddress());
                        intent.putExtra(ConstDB.COLUMN_LOCK, project.isLock());

                        intent.putExtra(ConstDB.COLUMN_COST_TYPE, project.getCostType().equals(Project.FIX_COST));

                        activity.startActivity(intent);
                        return true;

                    case R.id.op_remove_pro:
                        upDialogConfirmDelete(activity, project.getIdProject(), PROJECT);
                        return true;

                    case R.id.op_lock_unlock:
                        DataProjects.lockUnlock(activity, project, clientsPro);
                        ((MainActivity) activity).getFragment().onResume();
                        return true;
                }
                return false;
            }
        });
        menu.show();
    }

    public static void upPopupMenuAct(View v, final Activity activity, final home.lernesto.budgetquick.pojos.Activity act){
        PopupMenu menu = new PopupMenu(v.getContext(), v);
        menu.getMenuInflater().inflate(R.menu.popup_menu_act,menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.op_see_act:
                        intent = new Intent(activity, ExpenseMatActivity.class);
                        intent.putExtra(ConstDB.COLUMN_IDA, act.getIdActivity());
                        intent.putExtra(ConstDB.COLUMN_NAMEA, act.getNameAct());
                        intent.putExtra(ConstDB.COLUMN_UNIT, act.getUm());
                        activity.startActivity(intent);
                        return true;

                    case R.id.op_see_uses:
                        upDialogSeeUses(activity, act.getIdActivity());
                        return true;

                    case R.id.op_edit_act:
                        intent = new Intent(activity, EditActActivity.class);
                        intent.putExtra(ConstDB.COLUMN_IDA, act.getIdActivity());
                        intent.putExtra(ConstDB.COLUMN_NAMEA, act.getNameAct());
                        intent.putExtra(ConstDB.COLUMN_UNIT, act.getUm());
                        intent.putExtra(ConstDB.COLUMN_PRICE, act.getPrice());
                        intent.putExtra(ConstDB.COLUMN_CAT, act.getCategory());
                        activity.startActivity(intent);
                        return true;

                    case R.id.op_remove_act:
                        upDialogConfirmDelete(activity, act.getIdActivity(), ACTIVITY);
                        return true;
                }
                return false;
            }
        });
        menu.show();
    }

    public static void upPopupMenuMat(View v, final Activity activity, final Material material){
        PopupMenu menu = new PopupMenu(v.getContext(), v);
        menu.getMenuInflater().inflate(R.menu.popup_menu_basic, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.op_edit_basic:
                        Intent intent = new Intent(activity, EditMaterialActivity.class);
                        intent.putExtra(ConstDB.COLUMN_IDM, material.getIdMaterial());
                        intent.putExtra(ConstDB.COLUMN_NAMEM, material.getNameMaterial());
                        intent.putExtra(ConstDB.COLUMN_FORMAT, material.getFormat());
                        activity.startActivity(intent);
                        return true;

                    case R.id.op_remove_basic:
                        upDialogConfirmDelete(activity, material.getIdMaterial(), MATERIAL);
                        return true;
                }
                return false;
            }
        });
        menu.show();
    }

    public static void upPopupMenuCostAct(View v, final Activity activity, final CostActExtended costActExtended){
        PopupMenu menu = new PopupMenu(v.getContext(), v);
        menu.getMenuInflater().inflate(R.menu.popup_menu_cost_act, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.op_edit_cost_act:
                        Intent intent = new Intent(activity, EditCostActActivity.class);
                        intent.putExtra(ConstDB.COLUMN_IDP, activity.getIntent().getIntExtra(ConstDB.COLUMN_IDP, 0));
                        intent.putExtra(ConstDB.COLUMN_IDA,costActExtended.getCostAct().getIdAct());
                        intent.putExtra(ConstDB.COLUMN_NAMEP, activity.getIntent().getStringExtra(ConstDB.COLUMN_NAMEP));
                        intent.putExtra(ConstDB.COLUMN_ROOM, costActExtended.getCostAct().getRoom());
                        intent.putExtra(ConstDB.COLUMN_SIZE, costActExtended.getCostAct().getSize());
                        intent.putExtra(ConstDB.COLUMN_DUAL, costActExtended.getCostAct().isDual());
                        intent.putExtra(ConstDB.COLUMN_PROGRESS, costActExtended.getCostAct().getProgress());

                        activity.startActivity(intent);
                        return true;

                    case R.id.op_remove_cost_act:
                        upDialogConfirmDelete(activity, costActExtended, null, null, null, COST);
                        return true;

                    case R.id.op_progress_cost_act:
                        upDialogSetProgress(activity, costActExtended);
                        return true;
                }
                return false;
            }
        });
        menu.show();
    }

    public static void upPopupMenuExpenseMat(View v, final Activity activity, final ExpenseMatExtended expenseMat){
        PopupMenu menu = new PopupMenu(v.getContext(), v);
        menu.getMenuInflater().inflate(R.menu.popup_menu_basic, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.op_edit_basic:
                        Intent intent = new Intent(activity, EditExpenseMatActivity.class);
                        intent.putExtra(ConstDB.COLUMN_IDA, expenseMat.getExpenseMat().getIdAct());
                        intent.putExtra(ConstDB.COLUMN_NAMEA, activity.getIntent().getStringExtra(ConstDB.COLUMN_NAMEA));
                        intent.putExtra(ConstDB.COLUMN_IDM, expenseMat.getExpenseMat().getIdMat());
                        intent.putExtra(ConstDB.COLUMN_AMOUNT, expenseMat.getExpenseMat().getAmount());

                        activity.startActivity(intent);
                        return true;

                    case R.id.op_remove_basic:
                        upDialogConfirmDelete(activity, null, expenseMat, null, null, EXPENSE);
                        return true;
                }
                return false;
            }
        });
        menu.show();
    }

    public static void upPopupMenuNote(View view, final Activity activity, final Note note){
        PopupMenu menu = new PopupMenu(view.getContext(), view);
        menu.getMenuInflater().inflate(R.menu.popup_menu_basic, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.op_edit_basic:
                        ((NotesActivity)activity).setFlag(NotesActivity.EDIT);
                        ((NotesActivity)activity).setSelectedIdN(note.getIdN());
                        ((NotesActivity)activity).setTextForToEdit(note.getBody());
                        return true;

                    case R.id.op_remove_basic:
                        upDialogConfirmDelete(activity, null, null, note, null,NOTE);
                        return true;
                }
                return false;
            }
        });
        menu.show();
    }

    public static void upPopupMenuContact(View view, final Activity activity, final Contact contact){
        PopupMenu menu = new PopupMenu(view.getContext(), view);
        menu.getMenuInflater().inflate(R.menu.popup_menu_basic, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.op_edit_basic:
                        Intent intent = new Intent(activity, EditContactActivity.class);
                        intent.putExtra(ConstDB.COLUMN_IDP, activity.getIntent().getIntExtra(ConstDB.COLUMN_IDP, 0));
                        intent.putExtra(ConstDB.COLUMN_NAMEP, activity.getIntent().getStringExtra(ConstDB.COLUMN_NAMEP));
                        intent.putExtra(ConstDB.COLUMN_TEL, contact.getPhone());
                        intent.putExtra(ConstDB.COLUMN_CONTACT, contact.getName());

                        activity.startActivity(intent);
                        return true;

                    case R.id.op_remove_basic:
                        upDialogConfirmDelete(activity, null, null, null, contact, CONTACT);
                        return true;
                }
                return false;
            }
        });
        menu.show();
    }

    private static void upDialogConfirmDelete(final Activity activity, final int id, final String table){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(R.string.delete)
                .setMessage(R.string.confirm_delete_long)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (table) {
                            case PROJECT:
                                DataProjects.delete(activity, id);
                                ((MainActivity) activity).getFragment().onResume();
                                break;
                            case ACTIVITY:
                                DataActivity.delete(activity, id);
                                ((MainActivity) activity).getFragment().onResume();
                                break;
                            case MATERIAL:
                                DataMaterials.delete(activity, id);
                                ((MainActivity) activity).getFragment().onResume();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static void upDialogConfirmDelete(
            final Activity activity, final CostActExtended costActExtended,
            final ExpenseMatExtended expenseMatExtended, final Note note, final Contact contact, final String table){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(R.string.delete)
                .setMessage(R.string.confirm_delete_short)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (table) {
                            case COST:
                                DataCostAct.deleteDirect(
                                        activity,
                                        costActExtended.getCostAct().getIdPro(),
                                        costActExtended.getCostAct().getIdAct(),
                                        costActExtended.getCostAct().getRoom()
                                );
                                ((CostActActivity) activity).resumeActivity();
                                break;

                            case EXPENSE:
                                DataExpenseMat.deleteDirect(activity,expenseMatExtended.getExpenseMat().getIdAct(),
                                        expenseMatExtended.getExpenseMat().getIdMat()
                                );
                                ((ExpenseMatActivity) activity).resumeActivity();
                                break;

                            case NOTE:
                                DataNotes.directDelete(activity, note.getIdP(), note.getIdN());
                                ((NotesActivity)activity).refreshRecyclerView();
                                break;

                            case CONTACT:
                                DataContact.directDelete(activity, contact.getIdP(), contact.getPhone());
                                ((ContactActivity)activity).resumeActivity();
                                break;
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void upDialogReminder(final Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(R.string.reminder)
                .setMessage(R.string.edited_reminder)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static void upDialogSeeUses(final Activity activity, int idAct){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        String uses = activity.getResources().getString(R.string.without_uses);

        if (DataCostAct.isUsed(activity, idAct))
            uses = CNS.convertNameListToString(DataCostAct.getUses(activity, idAct));

        builder.setTitle(R.string.uses)
                .setMessage(uses)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static void upDialogClientInfo(Activity activity, ClientsPro clientsPro){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(R.string.client_data)
                .setMessage(
                        activity.getResources().getString(R.string.name) + ": " + clientsPro.getName() + "\n\n"
                                + activity.getResources().getString(R.string.client_address) + ": " + clientsPro.getClientAddress())
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void upDialogShowTCost(Context context, int cost){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.total_cost)
                .setMessage("$" + cost)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void upDialogOpPlusOverWrite(
            final Context context, final int idPro, final int idAct, final double oldSize, final String um, final String room, final double newSize){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String message = context.getResources().getString(R.string.exist_cost_act) + " " + oldSize + " " + um + ".";

        builder.setTitle(R.string.important)
                .setMessage(message)
                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(R.string.overwrite, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataCostAct.overWriteSize(context, idPro, idAct, newSize, room);
                    }
                })
                .setPositiveButton(R.string.plus, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataCostAct.plusSize(context, idPro, idAct, newSize, room);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    public static void upDialogSetProgress(final Activity activity, final CostActExtended costActExtended){
        final boolean[] seekBarIsChanged = {false};
        final double[] newProgress = {0.0};
        String tittle;

        @SuppressLint("InflateParams") View view = activity.getLayoutInflater().inflate(R.layout.dialog_seekbar_set_progress, null);
        final SeekBar seekBarProgress = view.findViewById(R.id.seekBar_progress);
        final TextView textViewProgress = view.findViewById(R.id.textViewDialogProgress);

        if (!costActExtended.getUm().equals("u")){
            tittle = activity.getString(
                    R.string.define_progress) + " " + costActExtended.getCostAct().getSize() + costActExtended.getUm();
            textViewProgress.setText(costActExtended.getCostAct().getProgress() + costActExtended.getUm());
            seekBarProgress.setProgress((int)((costActExtended.getCostAct().getProgress()/costActExtended.getCostAct().getSize())*100));

            seekBarProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekBarIsChanged[0] = true;
                    newProgress[0] = (costActExtended.getCostAct().getSize()*progress) /100;
                    BigDecimal formater = BigDecimal.valueOf(newProgress[0]);
                    formater = formater.setScale(2,BigDecimal.ROUND_HALF_UP);
                    newProgress[0] = formater.doubleValue();
                    textViewProgress.setText( formater.doubleValue() + costActExtended.getUm());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
        else {
            tittle = activity.getString(R.string.define_progress) + " " + (int)costActExtended.getCostAct().getSize() + "u";
            textViewProgress.setText((int)costActExtended.getCostAct().getProgress() + "u");
            seekBarProgress.setMax((int) costActExtended.getCostAct().getSize());
            seekBarProgress.setProgress((int)costActExtended.getCostAct().getProgress());

            seekBarProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekBarIsChanged[0] = true;
                    newProgress[0] = progress;
                    textViewProgress.setText( progress + "u");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(tittle)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (seekBarIsChanged[0]){
                            costActExtended.getCostAct().setProgress(newProgress[0]);
                            DataCostAct.updateNewProgress(activity, costActExtended.getCostAct());
                            ((CostActActivity)activity).refreshList();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public static void upDialogWhatsNew(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.news)
                .setView(R.layout.dialog_whats_new)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
