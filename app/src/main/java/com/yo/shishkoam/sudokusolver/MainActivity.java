package com.yo.shishkoam.sudokusolver;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    GridView mainField;
    Context context = this;
    Field field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        field = FieldManager.getInstance().getCurrentField();
        if (field == null) {
            field = new Field();
            FieldManager.getInstance().setCurrentField(field);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String oldField = sharedPreferences.getString("field", null);
        final String[] data = field.restore(oldField);
        mainField = (GridView) findViewById(R.id.main_field);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item, R.id.button, data);
        mainField.setAdapter(adapter);
        mainField.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mainField.setSelector(R.drawable.selector);
        mainField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final int row = position / 9;
                final int column = position % 9;
                final EditText editText = new EditText(context);
//                editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setView(editText)
//                        .setNegativeButton("Cancel", null)
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                String number = editText.getText().toString();
//                                field.setNumber(row, column, Integer.valueOf(number));
//                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item, R.id.button, field.restore());
//                                mainField.setAdapter(adapter);
//                            }
//                        });
//                builder.show();
                SelectNumberDialogFragment selectNumberDialogFragment = new SelectNumberDialogFragment();
                selectNumberDialogFragment.setOnNumberSelectListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        field.setNumber(row, column, position);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item, R.id.button, field.restore());
                        mainField.setAdapter(adapter);
                    }
                });
                selectNumberDialogFragment.show(getSupportFragmentManager(), "tag");
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                field.simpleCheck();
                field.proposalUniqueCheck();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item, R.id.button, field.restore());
                mainField.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("field", field.getData());
        editor.commit();
    }
}
