package com.example.weather;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MyBottomSheet extends BottomSheetDialogFragment {
    private OnDialogListener listener;
    private EditText input;
    private Button btnOk;

    public static MyBottomSheet newInstance(){
        return new MyBottomSheet();
    }

    public void setDialogListener(OnDialogListener listener){
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.mybottomsheet, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        input = view.findViewById(R.id.find_location_tv);
        btnOk = view.findViewById(R.id.ok_btn_bottom_fragment);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                String data = input.getText().toString();
                if(listener!=null) listener.onDialogOk(data);
            }
        });
    }
}
