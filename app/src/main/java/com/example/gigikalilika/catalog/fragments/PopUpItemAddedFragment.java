package com.example.gigikalilika.catalog.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.gigikalilika.catalog.R;

import net.steamcrafted.materialiconlib.MaterialIconView;

import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class PopUpItemAddedFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private TextView textViewText;

    private Button buttonShowOrderList;

    @Setter
    private ButtonClickListener buttonClickListener;

    @Setter
    private String text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        }
        return inflater.inflate(R.layout.layout_popup_item_added, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (view != null) {
            view.setOnClickListener(this);
            textViewText = view.findViewById(R.id.textViewText);


            MaterialIconView materialIconViewClosePopUp = view.findViewById(R.id.materialIconViewClosePopUp);
            materialIconViewClosePopUp.setOnClickListener(this);

            buttonShowOrderList = view.findViewById(R.id.buttonShowOrderList);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        textViewText.setText(text);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(buttonShowOrderList)) {
            if (buttonClickListener != null) {
                buttonClickListener.buttonShowOrderListClick(view);
            }
        } else {
            dismiss();
        }
    }

    public interface ButtonClickListener {
        void buttonShowOrderListClick(View view);
    }
}