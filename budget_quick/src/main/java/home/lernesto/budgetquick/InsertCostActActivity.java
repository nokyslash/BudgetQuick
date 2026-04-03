package home.lernesto.budgetquick;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.shawnlin.numberpicker.NumberPicker;

import java.math.BigDecimal;
import java.util.ArrayList;

import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.db.DataActivity;
import home.lernesto.budgetquick.db.DataCostAct;
import home.lernesto.budgetquick.pojos.CostAct;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class InsertCostActActivity extends AppCompatActivity {
//    Visual Components Variables
    private Toolbar toolbar;
    private Spinner spnCat, spnAct, spnRRooms;
    private EditText editRoom, editMU, editM2, editM3;
    private CheckBox chbDual, chbReuse;
    private Button btnInsert;
    private TextView txvDimens, txvTagDimenAmount, txvTotal;
    private LinearLayout llhEdit, llhInfo;
    private NumberPicker npMul;

//    Logical Variables
    private Activity activity;
    private Context context;
    private ArrayList<home.lernesto.budgetquick.pojos.Activity> activities;
    private int idP;
    private String nameP;
    private boolean fixCost;

    private int index = 0;
    private double total = 0.00;
    private String dimens = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_cost_act);

//        To link file XML
        toolbar                 = findViewById(R.id.appbar_inser_cost_act);
        spnCat                  = findViewById(R.id.spn_cat);
        spnAct                  = findViewById(R.id.spn_act);
        spnRRooms               = findViewById(R.id.spn_rRooms);
        editRoom                = findViewById(R.id.edit_room);
        editMU                  = findViewById(R.id.edit_mu);
        editM2                  = findViewById(R.id.edit_m2);
        editM3                  = findViewById(R.id.edit_m3);
        chbDual                 = findViewById(R.id.chb_dual);
        chbReuse                = findViewById(R.id.chb_reuse);
        btnInsert               = findViewById(R.id.btn_insert);
        ImageButton btnPlus     = findViewById(R.id.btn_accumulate);
        ImageButton btnDiscount = findViewById(R.id.btn_discount);
        txvDimens               = findViewById(R.id.txv_dimen_amount);
        txvTagDimenAmount       = findViewById(R.id.txv_tag_dimen_amount);
        txvTotal                = findViewById(R.id.txv_total);
        llhEdit                 = findViewById(R.id.llh_component_edit);
        llhInfo                 = findViewById(R.id.llh_component_info);
        npMul                   = findViewById(R.id.number_picker_mul);

        activity = this;
        context  = this;

//        Extract Extras
        idP      = getIntent().getIntExtra(ConstDB.COLUMN_IDP, 0);
        nameP    = getIntent().getStringExtra(ConstDB.COLUMN_NAMEP);
        fixCost  = getIntent().getBooleanExtra(ConstDB.COLUMN_COST_TYPE, false);

        initializeToolbar();
        initializeSpinnerCat();
        initializeSpinnerRRooms();
        initializeCheckBox();
        initializeButton();

        spnCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshSpinnerAct(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnAct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reset();
                if (!activities.isEmpty()) setSupport(activities.get(position).getUm());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        chbReuse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editRoom.setVisibility(View.INVISIBLE);
                    spnRRooms.setVisibility(View.VISIBLE);
                    editRoom.setText("");
                }
                else {
                    editRoom.setVisibility(View.VISIBLE);
                    spnRRooms.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomSelected = editRoom.getText().toString();
                if (chbReuse.isChecked())roomSelected = spnRRooms.getSelectedItem().toString();
                double size = DataCostAct.getSize(context, idP,
                        activities.get(spnAct.getSelectedItemPosition()).getIdActivity(),
                        roomSelected
                );

                if (txvTotal.getText().toString().isEmpty()) {
                    switch (activities.get(spnAct.getSelectedItemPosition()).getUm()) {
                        case "u":
                        case "m":
                            if (!editMU.getText().toString().isEmpty() && (!editRoom.getText().toString().isEmpty() ||
                                    chbReuse.isChecked())) {
                                total += Double.parseDouble(editMU.getText().toString());
                                if (total > 0) {
                                    if (size == 0) insert(v);
                                    else {
                                        hideKeyboard();
                                        MDNNS.upDialogOpPlusOverWrite(
                                                context,
                                                idP,
                                                activities.get(spnAct.getSelectedItemPosition()).getIdActivity(),
                                                size,
                                                activities.get(spnAct.getSelectedItemPosition()).getUm(),
                                                roomSelected,
                                                formatTotal()
                                        );
                                        fullReset();
                                        MDNNS.upNotificationEdited(activity, v);
                                    }
                                } else MDNNS.upNotificationInvalidTotal(v);
                            } else MDNNS.upNotificationEmptyField(v);
                            break;

                        case "m²":
                            if (!editMU.getText().toString().isEmpty() && !editM2.getText().toString().isEmpty() &&
                                    (!editRoom.getText().toString().isEmpty() || chbReuse.isChecked())) {
                                total += Double.parseDouble(editMU.getText().toString()) *
                                        Double.parseDouble(editM2.getText().toString());
                                if (total > 0) {
                                    if (size == 0) insert(v);
                                    else {
                                        hideKeyboard();
                                        MDNNS.upDialogOpPlusOverWrite(
                                                context,
                                                idP,
                                                activities.get(spnAct.getSelectedItemPosition()).getIdActivity(),
                                                size,
                                                activities.get(spnAct.getSelectedItemPosition()).getUm(),
                                                roomSelected,
                                                formatTotal()
                                        );
                                        fullReset();
                                        MDNNS.upNotificationEdited(activity, v);
                                    }
                                } else MDNNS.upNotificationInvalidTotal(v);
                            } else MDNNS.upNotificationEmptyField(v);
                            break;

                        case "m³":
                            if (!editMU.getText().toString().isEmpty() && !editM2.getText().toString().isEmpty() &&
                                    !editM3.getText().toString().isEmpty() &&
                                    (!editRoom.getText().toString().isEmpty() || chbReuse.isChecked())) {
                                total += Double.parseDouble(editMU.getText().toString()) *
                                        Double.parseDouble(editM2.getText().toString()) *
                                        Double.parseDouble(editM3.getText().toString());
                                if (total > 0) {
                                    if (size == 0) insert(v);
                                    else {
                                        hideKeyboard();
                                        MDNNS.upDialogOpPlusOverWrite(
                                                context,
                                                idP,
                                                activities.get(spnAct.getSelectedItemPosition()).getIdActivity(),
                                                size,
                                                activities.get(spnAct.getSelectedItemPosition()).getUm(),
                                                roomSelected,
                                                formatTotal()
                                        );
                                        fullReset();
                                        MDNNS.upNotificationEdited(activity, v);
                                    }
                                } else MDNNS.upNotificationInvalidTotal(v);
                            } else MDNNS.upNotificationEmptyField(v);
                            break;
                    }
                } else {
                    if (!editRoom.getText().toString().isEmpty() || chbReuse.isChecked()) {
                        if (total > 0) {
                            if (size == 0) insert(v);
                            else {
                                hideKeyboard();
                                MDNNS.upDialogOpPlusOverWrite(
                                        context,
                                        idP,
                                        activities.get(spnAct.getSelectedItemPosition()).getIdActivity(),
                                        size,
                                        activities.get(spnAct.getSelectedItemPosition()).getUm(),
                                        roomSelected,
                                        formatTotal()
                                );
                                fullReset();
                                MDNNS.upNotificationEdited(activity, v);
                            }
                        } else MDNNS.upNotificationInvalidTotal(v);
                    } else MDNNS.upNotificationEmptyFieldRoom(v);
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                switch (activities.get(spnAct.getSelectedItemPosition()).getUm()) {
                    case "u":
                        if (!editMU.getText().toString().isEmpty()) {
                            total += Double.parseDouble(editMU.getText().toString());
                            index++;
                            dimens += index + " : " + editMU.getText().toString() + "\n";
                            txvDimens.setText(dimens);
                            txvTotal.setText((int) total + " u");
                            parcialReset();
                        }
                        break;

                    case "m":
                        if (!editMU.getText().toString().isEmpty()) {
                            total += Double.parseDouble(editMU.getText().toString()) * npMul.getValue();
                            index++;
                            txvDimens.setText(dimens += index + " : (" + editMU.getText().toString() + ") x " +
                                    npMul.getValue() + "\n");
                            txvTotal.setText(formatTotal() + " m");
                            parcialReset();
                        }
                        break;

                    case "m²":
                        if (!editMU.getText().toString().isEmpty() && !editM2.getText().toString().isEmpty()) {

                            total += Double.parseDouble(editMU.getText().toString()) *
                                    Double.parseDouble(editM2.getText().toString()) *
                                    npMul.getValue();

                            index++;
                            txvDimens.setText(
                                    dimens += index + " : (" + editMU.getText().toString() + "  x  " +
                                            editM2.getText().toString() + ") x " + npMul.getValue() + "\n"
                            );

                            txvTotal.setText(formatTotal() + " m²");
                            parcialReset();
                        } else MDNNS.upNotificationEmptyField(v);
                        break;

                    case "m³":
                        if (!editMU.getText().toString().isEmpty() && !editM2.getText().toString().isEmpty() &&
                                !editM3.getText().toString().isEmpty()) {

                            total += Double.parseDouble(editMU.getText().toString()) *
                                    Double.parseDouble(editM2.getText().toString()) *
                                    Double.parseDouble(editM3.getText().toString()) *
                                    npMul.getValue();

                            index++;
                            txvDimens.setText(
                                    dimens += index + " : (" + editMU.getText().toString() + "  x  " +
                                            editM2.getText().toString() + "  x  " +
                                            editM3.getText().toString() + ") x " + npMul.getValue() + "\n"
                            );

                            txvTotal.setText(formatTotal() + " m³");
                            parcialReset();
                        } else MDNNS.upNotificationEmptyField(v);
                        break;
                }
            }
        });

        btnDiscount.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                switch (activities.get(spnAct.getSelectedItemPosition()).getUm()) {
                    case "u":
                        if (!editMU.getText().toString().isEmpty()) {
                            total -= Double.parseDouble(editMU.getText().toString());
                            index++;
                            dimens += index + " : - ( " + editMU.getText().toString() + " )\n";
                            txvDimens.setText(dimens);
                            txvTotal.setText((int) total + " u");
                            parcialReset();
                        }
                        break;

                    case "m":
                        if (!editMU.getText().toString().isEmpty()) {
                            total -= Double.parseDouble(editMU.getText().toString()) * npMul.getValue();
                            index++;
                            txvDimens.setText(
                                    dimens += index + " : - ( " + editMU.getText().toString() + " ) x " +
                                            npMul.getValue() + "\n");
                            txvTotal.setText(formatTotal() + " m");
                            parcialReset();
                        }
                        break;

                    case "m²":
                        if (!editMU.getText().toString().isEmpty() && !editM2.getText().toString().isEmpty()) {

                            total -= Double.parseDouble(editMU.getText().toString()) *
                                    Double.parseDouble(editM2.getText().toString()) *
                                    npMul.getValue();

                            index++;
                            txvDimens.setText(
                                    dimens += index + " : - ( " + editMU.getText().toString() + "  x  " +
                                            editM2.getText().toString() + " ) x " + npMul.getValue() + "\n"
                            );

                            txvTotal.setText(formatTotal() + " m²");
                            parcialReset();
                        } else MDNNS.upNotificationEmptyField(v);
                        break;

                    case "m³":
                        if (!editMU.getText().toString().isEmpty() && !editM2.getText().toString().isEmpty() &&
                                !editM3.getText().toString().isEmpty()) {

                            total -= Double.parseDouble(editMU.getText().toString()) *
                                    Double.parseDouble(editM2.getText().toString()) *
                                    Double.parseDouble(editM3.getText().toString()) *
                                    npMul.getValue();

                            index++;
                            txvDimens.setText(
                                    dimens += index + " : - ( " + editMU.getText().toString() + "  x  " +
                                            editM2.getText().toString() + " x " +
                                            editM3.getText().toString() + " ) x " + npMul.getValue() + "\n"
                            );

                            txvTotal.setText(formatTotal() + " m³");
                            parcialReset();
                        } else MDNNS.upNotificationEmptyField(v);
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void initializeToolbar(){
        if (toolbar != null){
            toolbar.setTitle(getString(R.string.to_do) + " " + nameP);
            setSupportActionBar(toolbar);
        }
    }

    private void initializeSpinnerCat(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.categories_total,
                R.layout.support_simple_spinner_dropdown_item);

        spnCat.setAdapter(adapter);
        refreshSpinnerAct(spnCat.getSelectedItem().toString());
    }

    private void initializeSpinnerRRooms(){
        String[] rooms = DataCostAct.getRooms(this, idP);
        if (rooms.length != 0){
            chbReuse.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    R.layout.support_simple_spinner_dropdown_item,
                    rooms
            );
            spnRRooms.setAdapter(adapter);
        }
    }

    private void refreshSpinnerAct(String cat){
        activities = DataActivity.getActivities(this,cat);
        String[] namePriceAndExpense;

        if (!activities.isEmpty()){
            namePriceAndExpense = new String[activities.size()];

            for (int i = 0; i < activities.size(); i++) {
                if (DataCostAct.containsExpenseMat(this, activities.get(i).getIdActivity())) {
                    namePriceAndExpense[i] = activities.get(i).getNameAct() + " - $" +
                            activities.get(i).getPrice() + " - " + getString(R.string.without_expense);
                } else {
                    namePriceAndExpense[i] = activities.get(i).getNameAct() + " - $" +
                            activities.get(i).getPrice() + " - " + getString(R.string.with_expense);
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, R.layout.support_simple_spinner_dropdown_item, namePriceAndExpense);

            spnAct.setAdapter(adapter);
            spnAct.setVisibility(View.VISIBLE);
            editRoom.setEnabled(true);
            chbDual.setEnabled(true);
            btnInsert.setEnabled(true);
            llhEdit.setVisibility(View.VISIBLE);
            llhInfo.setVisibility(View.VISIBLE);
            reset();
            setSupport(activities.get(spnAct.getSelectedItemPosition()).getUm());
        } else {
            editRoom.setEnabled(false);
            chbDual.setEnabled(false);
            btnInsert.setEnabled(false);
            spnAct.setVisibility(View.INVISIBLE);
            llhEdit.setVisibility(View.INVISIBLE);
            llhInfo.setVisibility(View.INVISIBLE);
            hideKeyboard();
            MDNNS.upNotificationThereIsNotElements(spnCat);
        }
    }

    private void setSupport(String um){
        switch (um){
            case "u":
                setSupportForU();
                break;

            case "m":
                setSupportForM();
                break;

            case "m²":
                setSupportForM2();
                break;

            case "m³":
                setSupportForM3();
                break;
        }
    }

    private void setSupportForU(){
        editMU.setInputType(InputType.TYPE_CLASS_NUMBER);
        editMU.setHint(R.string.amount);
        editM2.setVisibility(View.INVISIBLE);
        editM3.setVisibility(View.INVISIBLE);
        npMul.setVisibility(View.INVISIBLE);

        txvTagDimenAmount.setText(R.string.amounts);
    }

    private void setSupportForM(){
        editMU.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editMU.setHint(R.string.large);
        editM2.setVisibility(View.INVISIBLE);
        editM3.setVisibility(View.INVISIBLE);
        npMul.setVisibility(View.VISIBLE);

        txvTagDimenAmount.setText(R.string.dimens);
    }

    private void setSupportForM2(){
        editMU.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editMU.setHint(R.string.large);
        editM2.setVisibility(View.VISIBLE);
        editM3.setVisibility(View.INVISIBLE);
        npMul.setVisibility(View.VISIBLE);

        txvTagDimenAmount.setText(R.string.dimens);
    }

    private void setSupportForM3(){
        editMU.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editMU.setHint(R.string.large);
        editM2.setVisibility(View.VISIBLE);
        editM3.setVisibility(View.VISIBLE);
        npMul.setVisibility(View.VISIBLE);

        txvTagDimenAmount.setText(R.string.dimens);
    }

    private void initializeCheckBox(){
        if (fixCost){
            chbDual.setVisibility(View.GONE);
        }
        else {
            chbDual.setVisibility(View.VISIBLE);
        }
        chbDual.setChecked(true);
    }

    private void initializeButton(){
        btnInsert.setText(R.string.insert);
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        assert imm != null;
        imm.hideSoftInputFromWindow(editRoom.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editMU.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editM2.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editM3.getWindowToken(), 0);
    }

    private void parcialReset(){
        if (!editMU.getText().toString().isEmpty())YoYo.with(Techniques.Shake).playOn(findViewById(R.id.edit_mu));
        if (!editM2.getText().toString().isEmpty())YoYo.with(Techniques.Shake).playOn(findViewById(R.id.edit_m2));
        if (!editM3.getText().toString().isEmpty())YoYo.with(Techniques.Shake).playOn(findViewById(R.id.edit_m3));
        editMU.setText("");
        editM2.setText("");
        editM3.setText("");
        npMul.setValue(1);
    }

    private void reset(){
        initializeSpinnerRRooms();
        if (chbDual.isEnabled())
            chbDual.setChecked(true);
        txvDimens.setText("");
        txvTotal.setText("");

        index = 0;
        total = 0.00;
        dimens = "";

        parcialReset();
    }

    private void fullReset(){
        spnAct.setSelection(0);

        reset();
    }

    private void insert(View view){
        hideKeyboard();
        String roomSelected = editRoom.getText().toString();
        if (chbReuse.isChecked())roomSelected = spnRRooms.getSelectedItem().toString();
        DataCostAct.insert(
                this,
                new CostAct(
                        idP,
                        activities.get(spnAct.getSelectedItemPosition()).getIdActivity(),
                        formatTotal(),
                        roomSelected,
                        chbDual.isChecked(),
                        CostAct.DEFAULT_PROGRESS)
        );
        fullReset();
        MDNNS.upNotificationInserted(view);
    }

    private double formatTotal(){
        BigDecimal formater = new BigDecimal(total);
        formater = formater.setScale(2,BigDecimal.ROUND_HALF_UP);
        return formater.doubleValue();
    }
}
