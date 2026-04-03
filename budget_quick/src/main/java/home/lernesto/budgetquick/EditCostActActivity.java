package home.lernesto.budgetquick;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shawnlin.numberpicker.NumberPicker;

import java.math.BigDecimal;

import home.lernesto.budgetquick.db.DataActivity;
import home.lernesto.budgetquick.db.DataCostAct;
import home.lernesto.budgetquick.db.ConstDB;
import home.lernesto.budgetquick.pojos.Activity;
import home.lernesto.budgetquick.pojos.CostAct;
import home.lernesto.budgetquick.utilsns.MDNNS;

public class EditCostActActivity extends AppCompatActivity {

//    Visual Variables
    private Toolbar toolbar;
    private Spinner spnCat, spnAct, spnRRooms;
    private EditText editRoom, editMU, editM2, editM3;
    private CheckBox chbDual, chbReuse;
    private Button btnEdit;
    private TextView txvTagDimenAmount;
    private TextView txvTotal;
    private NumberPicker npMul;

//    Logical Variables
    private android.app.Activity activity;
    private Context context;
    private int idP, idA;
    private String nameP, room;
    private boolean dual;
    private double progress;
    private Activity act;

    private double total;
    private int index = 0;
    private String dimens = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_cost_act);

        activity                 = this;
        context                  = this;

//        To link file XML
        toolbar                  = findViewById(R.id.appbar_inser_cost_act);
        spnCat                   = findViewById(R.id.spn_cat);
        spnAct                   = findViewById(R.id.spn_act);
        spnRRooms               = findViewById(R.id.spn_rRooms);
        editRoom                 = findViewById(R.id.edit_room);
        editMU                   = findViewById(R.id.edit_mu);
        editM2                   = findViewById(R.id.edit_m2);
        editM3                   = findViewById(R.id.edit_m3);
        chbDual                  = findViewById(R.id.chb_dual);
        chbReuse                = findViewById(R.id.chb_reuse);
        btnEdit                  = findViewById(R.id.btn_insert);
        ImageButton btnPlus      = findViewById(R.id.btn_accumulate);
        ImageButton btnDiscount  = findViewById(R.id.btn_discount);
        final TextView txvDimens = findViewById(R.id.txv_dimen_amount);
        txvTagDimenAmount        = findViewById(R.id.txv_tag_dimen_amount);
        txvTotal                 = findViewById(R.id.txv_total);
        npMul                    = findViewById(R.id.number_picker_mul);

//        Extract Extras
        idP      = getIntent().getIntExtra(ConstDB.COLUMN_IDP, 0);
        idA      = getIntent().getIntExtra(ConstDB.COLUMN_IDA, 0);
        nameP    = getIntent().getStringExtra(ConstDB.COLUMN_NAMEP);
        room     = getIntent().getStringExtra(ConstDB.COLUMN_ROOM);
        total    = getIntent().getDoubleExtra(ConstDB.COLUMN_SIZE, 0.0);
        dual     = getIntent().getBooleanExtra(ConstDB.COLUMN_DUAL, false);
        progress = getIntent().getDoubleExtra(ConstDB.COLUMN_PROGRESS,0.0);

        act = DataActivity.findAct(this, idA);

        initializeToolbar();
        initializeSpinnerCat();
        initializeSpinnerAct();
        initializeSpinnerRRooms();
        initializeEditTextRoom();
        setSupport();
        initializeCheckBox();
        initializeButtonEdit();
        initializeTextViewTotal();

        chbReuse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    spnRRooms.setVisibility(View.VISIBLE);
                    editRoom.setText("");
                }
                else spnRRooms.setVisibility(View.INVISIBLE);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomSelected = editRoom.getText().toString();
                if (chbReuse.isChecked())roomSelected = spnRRooms.getSelectedItem().toString();
                if (!editRoom.getText().toString().isEmpty() || chbReuse.isChecked()) {
                    double size = DataCostAct.getSize(context, idP, idA, roomSelected);

                    if (total > 0){
                        if (room.equals(roomSelected)) edit(v);
                        else if (size == 0) edit(v);
                        else {
                            hideKeyboard();
                            MDNNS.upDialogOpPlusOverWrite(
                                    context, idP, idA, size, act.getUm(), roomSelected, formatTotal());
                            MDNNS.upNotificationEdited(activity, v);
                        }
                    } else MDNNS.upNotificationInvalidTotal(v);
                } else MDNNS.upNotificationEmptyFieldRoom(v);
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                switch (act.getUm()) {
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
                switch (act.getUm()) {
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
        super.onBackPressed();
        finish();
    }

    private void initializeToolbar(){
        if (toolbar != null){
            toolbar.setTitle(getString(R.string.edit_to_do) + " " + nameP);
            setSupportActionBar(toolbar);
        }
    }

    private void initializeSpinnerCat(){
        String[] cat = new String[]{act.getCategory()};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, cat);

        spnCat.setAdapter(adapter);
        spnCat.setEnabled(false);
    }

    private void initializeSpinnerAct(){
        String[] act = new String[1];
        if (DataCostAct.containsExpenseMat(this, idA))
            act[0] = this.act.getNameAct() + " - " + this.act.getPrice() + " - " + getString(R.string.with_expense);
        else act[0] = this.act.getNameAct() + " - " + this.act.getPrice() + " - " + getString(R.string.without_expense);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, act);

        spnAct.setAdapter(adapter);
        spnAct.setEnabled(false);
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

    private void initializeEditTextRoom(){
        editRoom.setText(room);
    }

    private void setSupport(){
        switch (act.getUm()){
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
        editMU.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        editMU.setHint(R.string.amount);
        editM2.setVisibility(View.INVISIBLE);
        editM2.setHint("");
        editM3.setVisibility(View.INVISIBLE);
        editM3.setHint("");

        txvTagDimenAmount.setText(R.string.amounts);
    }

    private void setSupportForM(){
        editMU.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
                | InputType.TYPE_NUMBER_FLAG_SIGNED);
        editMU.setHint(R.string.large);
        editM2.setVisibility(View.INVISIBLE);
        editM2.setHint("");
        editM3.setVisibility(View.INVISIBLE);
        editM3.setHint("");

        txvTagDimenAmount.setText(R.string.dimens);
    }

    private void setSupportForM2(){
        editMU.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
                | InputType.TYPE_NUMBER_FLAG_SIGNED);
        editMU.setHint(R.string.large);
        editM2.setVisibility(View.VISIBLE);
        editM2.setHint(R.string.width);
        editM3.setVisibility(View.INVISIBLE);
        editM3.setHint("");

        txvTagDimenAmount.setText(R.string.dimens);
    }

    private void setSupportForM3(){
        editMU.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
                | InputType.TYPE_NUMBER_FLAG_SIGNED);
        editMU.setHint(R.string.large);
        editM2.setVisibility(View.VISIBLE);
        editM2.setHint(R.string.width);
        editM3.setVisibility(View.VISIBLE);
        editM3.setHint(R.string.height);

        txvTagDimenAmount.setText(R.string.dimens);
    }

    private void initializeCheckBox(){
        chbDual.setChecked(dual);
    }

    private void initializeButtonEdit(){
        btnEdit.setText(R.string.edit);
    }

    @SuppressLint("SetTextI18n")
    private void initializeTextViewTotal(){
        txvTotal.setText(total + " " + act.getUm());
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        assert imm != null;
        imm.hideSoftInputFromWindow(editRoom.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editMU.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editM2.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editM3.getWindowToken(), 0);
    }

    private void edit(View view){
        DataCostAct.edit(
                this,
                new CostAct(idP, idA, formatTotal(), editRoom.getText().toString(), chbDual.isChecked(),progress),
                idA
        );
        hideKeyboard();
        MDNNS.upNotificationEdited(this, view);
    }

    private double formatTotal(){
        BigDecimal formater = new BigDecimal(total);
        formater = formater.setScale(2,BigDecimal.ROUND_UP);
        return formater.doubleValue();
    }

    private void parcialReset(){
        editMU.setText("");
        editM2.setText("");
        editM3.setText("");
    }
}
