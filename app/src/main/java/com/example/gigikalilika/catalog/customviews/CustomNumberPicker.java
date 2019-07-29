package com.example.gigikalilika.catalog.customviews;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gigikalilika.catalog.R;
import com.example.gigikalilika.catalog.constants.Constants;

import net.steamcrafted.materialiconlib.MaterialIconView;

import lombok.Setter;

public class CustomNumberPicker extends LinearLayout implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener {

    @Setter
    private CustomNumberPickerClickListener customNumberPickerClickListener;

    private MaterialIconView materialIconViewMinus;

    private MaterialIconView materialIconViewPlus;

    private EditText editTextQuantity;

    public CustomNumberPicker(Context context) {
        super(context);
        super.setOnClickListener(this);
        init(context);
    }

    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_quantity_selector, this);
        setup();
        initComponents();
    }

    private void setup() {
        materialIconViewMinus = findViewById(R.id.materialIconViewMinus);
        materialIconViewPlus = findViewById(R.id.materialIconViewPlus);
        editTextQuantity = findViewById(R.id.editTextQuantity);
    }

    private void initComponents() {
        materialIconViewMinus.setOnClickListener(this);

        materialIconViewPlus.setOnClickListener(this);

        editTextQuantity.setOnClickListener(this);

        editTextQuantity.setOnEditorActionListener(this);

        editTextQuantity.addTextChangedListener(this);

        editTextQuantity.setFilters(new InputFilter[]{(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) -> {
            try {
                String newVal = dest.toString().substring(0, dstart) + dest.toString().substring(dend);

                newVal = newVal.substring(0, dstart) + source.toString() + newVal.substring(dstart);
                int input = Integer.parseInt(newVal);
                if (input >= Constants.QUANTITY_PICKER_MINIMUM_VALUE && input <= Constants.QUANTITY_PICKER_MAXIMUM_VALUE) {
                    return null;
                }
            } catch (NumberFormatException ignored) {
            }
            return "";
        }});
    }

    private void fontTextViewMinusClick() {
        int currentValue = getEditTextQuantityCurrentValue();
        if (currentValue > Constants.QUANTITY_PICKER_MINIMUM_VALUE) {
            editTextQuantity.setText(String.valueOf(currentValue - 1));
        }
    }

    private void fontTextViewPlusClick() {
        int currentValue = getEditTextQuantityCurrentValue();
        if (currentValue < Constants.QUANTITY_PICKER_MAXIMUM_VALUE) {
            editTextQuantity.setText(String.valueOf(currentValue + 1));
        }
    }

    private int getEditTextQuantityCurrentValue() {
        try {
            return Integer.parseInt(editTextQuantity.getText().toString());
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(materialIconViewMinus)) {
            fontTextViewMinusClick();
        } else if (view.equals(materialIconViewPlus)) {
            fontTextViewPlusClick();
        }
        if (customNumberPickerClickListener != null) {
            customNumberPickerClickListener.onCustomNumberPickerClick(view);
        }
    }

    public void setValue(int quantity) {
        editTextQuantity.setText(String.valueOf(quantity));
    }

    public int getValue() {
        return getEditTextQuantityCurrentValue();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s != null) {
            int value;
            try {
                value = Integer.valueOf(s.toString());
                if (value == 0) {
                    value = Constants.QUANTITY_PICKER_MINIMUM_VALUE;
                    editTextQuantity.setText(String.valueOf(value));
                }
            } catch (NumberFormatException nfe) {
                value = Constants.QUANTITY_PICKER_MINIMUM_VALUE;
                editTextQuantity.setText(String.valueOf(value));
            }
        }
        if (customNumberPickerClickListener != null) {
            customNumberPickerClickListener.onCustomNumberPickerClick(null);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if ((actionId == EditorInfo.IME_ACTION_DONE)) {

            // hide virtual keyboard
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
            return true;
        }
        return false;

    }

    public interface CustomNumberPickerClickListener {
        void onCustomNumberPickerClick(View view);
    }
}