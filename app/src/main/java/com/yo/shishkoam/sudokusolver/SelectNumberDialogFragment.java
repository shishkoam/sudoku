package com.yo.shishkoam.sudokusolver;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

/**
 * Created by User on 13.02.2017 to save dialog during rotation
 */

public class SelectNumberDialogFragment extends DialogFragment {
    private Context context;
    private AdapterView.OnItemClickListener onNumberSelectListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.set_number_dialog, null);
        GridView mainField = (GridView) content.findViewById(R.id.main_field);
        final String[] data = {"<-","1","2","3","4","5","6","7","8","9"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.item, R.id.button, data);
        mainField.setAdapter(adapter);
        mainField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (onNumberSelectListener != null) {
                    onNumberSelectListener.onItemClick(parent, view, position, id);
                }
            }
        });
        getDialog().setTitle("Choose number:");
        getDialog().setContentView(content);
        return container;
    }

    public void setOnNumberSelectListener(AdapterView.OnItemClickListener listener) {
        onNumberSelectListener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
