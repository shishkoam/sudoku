package com.yo.shishkoam.sudokusolver;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anton on 03.11.17.
 */

public class GridArrayAdapter extends ArrayAdapter<String[]> {

    private AppCompatActivity activity;
    private Field field;
    private String[] data;
    private ArrayList<String[]> datasets = new ArrayList<>();
    public GridArrayAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public GridArrayAdapter(AppCompatActivity context, @NonNull String[] objects, Field field) {
        super(context, R.layout.item_grid, R.id.button);
        this.activity = context;
        this.field = field;
        this.data = objects;
        for (int i = 0;i < 9; i++) {
            datasets.add(new String[9]);
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                datasets.get(i/3 + j/3)[i%3+j%3] = data[i*9+j];
            }
        }
        for (int i = 0;i < 9; i++) {
            add(datasets.get(i));
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = View.inflate(getContext(), R.layout.item_grid, null);
        final GridView mainField = (GridView) v.findViewById(R.id.first_field);
        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), R.layout.item_grid, R.id.button, datasets.get(position));
        mainField.setAdapter(adapter);
        mainField.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mainField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int pos, long id) {
                    final int row = (position *3 + pos) / 9;
                    final int column = (position *3 + pos) % 9;
                    SelectNumberDialogFragment selectNumberDialogFragment = new SelectNumberDialogFragment();
                    selectNumberDialogFragment.setOnNumberSelectListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            field.setNumber(row, column, pos);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.item, R.id.button, field.restore());
                            mainField.setAdapter(adapter);
                        }
                    });
            selectNumberDialogFragment.show(activity.getSupportFragmentManager(), "tag");
                }
            });
        return v;
    }

}
