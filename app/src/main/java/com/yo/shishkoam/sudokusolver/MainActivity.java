package com.yo.shishkoam.sudokusolver;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    GridView mainField;
    Context context = this;
    Field field;
//    static final String ANDROID_DATA_DIR = "/data/data/com.yo.shishkoam.sudokusolver";

    private static final int REQUEST_IMAGE = 100;
    private static final int STORAGE=1;
    private String ANDROID_DATA_DIR;
    private static File destination;
    private TextView resultTextView;
    private ImageView imageView;

//    final String openAlprConfFile = ANDROID_DATA_DIR + File.separatorChar + "runtime_data" + File.separatorChar + "openalpr.conf";

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
        initFeild1(data);
        FloatingActionButton nextFab = (FloatingActionButton) findViewById(R.id.next_fab);
        nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean fieldChanged = field.simpleCheck();
                if (!fieldChanged) {
                    Toast.makeText(context, "This is unique method", Toast.LENGTH_SHORT).show();
                    field.proposalUniqueCheck();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item, R.id.button, field.restore());
                mainField.setAdapter(adapter);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.sync_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                field = new Field();
                FieldManager.getInstance().setCurrentField(field);
                initFeild1(field.restore());
            }
        });

        ANDROID_DATA_DIR = this.getApplicationInfo().dataDir;

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        resultTextView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);

        resultTextView.setText("Press the button below to start a request.");
//
//        String result = OpenALPR.Factory.create(MainActivity.this, ANDROID_DATA_DIR).recognizeWithCountryRegionNConfig("us", "", image.getAbsolutePath(), openAlprConfFile, 10);
    }

    private void initFeild(String[] data) {
        mainField = (GridView) findViewById(R.id.main_field);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item, R.id.button, data);
        mainField.setAdapter(adapter);
        mainField.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mainField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final int row = position / 9;
                final int column = position % 9;
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
    }

    private void initFeild1(String[] data) {
        mainField = (GridView) findViewById(R.id.main_field);
        mainField.setNumColumns(3);
        GridArrayAdapter adapter = new GridArrayAdapter(this, data ,field);
        mainField.setAdapter(adapter);
        mainField.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
//        mainField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                final int row = position / 9;
//                final int column = position % 9;
//                SelectNumberDialogFragment selectNumberDialogFragment = new SelectNumberDialogFragment();
//                selectNumberDialogFragment.setOnNumberSelectListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        field.setNumber(row, column, position);
//                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item, R.id.button, field.restore());
//                        mainField.setAdapter(adapter);
//                    }
//                });
//                selectNumberDialogFragment.show(getSupportFragmentManager(), "tag");
//            }
//        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            final ProgressDialog progress = ProgressDialog.show(this, "Loading", "Parsing result...", true);
            final String openAlprConfFile = ANDROID_DATA_DIR + File.separatorChar + "runtime_data" + File.separatorChar + "openalpr.conf";
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;

            // Picasso requires permission.WRITE_EXTERNAL_STORAGE
            Picasso.with(MainActivity.this).load(destination).fit().centerCrop().into(imageView);
            resultTextView.setText("Processing");
//
//            AsyncTask.execute(new Runnable() {
//                @Override
//                public void run() {
//                    String result = OpenALPR.Factory.create(MainActivity.this, ANDROID_DATA_DIR).recognizeWithCountryRegionNConfig("us", "", destination.getAbsolutePath(), openAlprConfFile, 10);
//
//                    Log.d("OPEN ALPR", result);
//
//                    try {
//                        final Results results = new Gson().fromJson(result, Results.class);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (results == null || results.getResults() == null || results.getResults().size() == 0) {
//                                    Toast.makeText(MainActivity.this, "It was not possible to detect the licence plate.", Toast.LENGTH_LONG).show();
//                                    resultTextView.setText("It was not possible to detect the licence plate.");
//                                } else {
//                                    resultTextView.setText("Plate: " + results.getResults().get(0).getPlate()
//                                            // Trim confidence to two decimal places
//                                            + " Confidence: " + String.format("%.2f", results.getResults().get(0).getConfidence()) + "%"
//                                            // Convert processing time to seconds and trim to two decimal places
//                                            + " Processing time: " + String.format("%.2f", ((results.getProcessingTimeMs() / 1000.0) % 60)) + " seconds");
//                                }
//                            }
//                        });
//
//                    } catch (JsonSyntaxException exception) {
//                        final ResultsError resultsError = new Gson().fromJson(result, ResultsError.class);
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                resultTextView.setText(resultsError.getMsg());
//                            }
//                        });
//                    }
//
//                    progress.dismiss();
//                }
//            });
        }
    }

    private void checkPermission() {
        List<String> permissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()) {
            Toast.makeText(this, "Storage access needed to manage the picture.", Toast.LENGTH_LONG).show();
            String[] params = permissions.toArray(new String[permissions.size()]);
            ActivityCompat.requestPermissions(this, params, STORAGE);
        } else { // We already have permissions, so handle as normal
            takePicture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case STORAGE:{
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for WRITE_EXTERNAL_STORAGE
                Boolean storage = perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (storage) {
                    // permission was granted, yay!
                    takePicture();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Storage permission is needed to analyse the picture.", Toast.LENGTH_LONG).show();
                }
            }
            default:
                break;
        }
    }

    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());

        return df.format(date);
    }

    public void takePicture() {
        // Use a folder to store all results
        File folder = new File(Environment.getExternalStorageDirectory() + "/OpenALPR/");
        if (!folder.exists()) {
            folder.mkdir();
        }

        // Generate the path for the next photo
        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        destination = new File(folder, name + ".jpg");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (destination != null) {// Picasso does not seem to have an issue with a null value, but to be safe
            Picasso.with(MainActivity.this).load(destination).fit().centerCrop().into(imageView);
        }
    }
}
