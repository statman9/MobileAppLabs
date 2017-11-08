package com.example.josh.lab2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.EditText;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.content.DialogInterface;

import java.util.Calendar;


/**
 * Created by Josh on 9/30/2017.
 */

public class StartTabActivity extends ListActivity {

    private CommentsDataSource datasource = new CommentsDataSource(this);
    Calendar datetime = Calendar.getInstance();
    double activityDuration = 0;
    int activityDistance = 0;
    int activityCalories = 0;
    int activityHeartRate = 0;
    String comment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        datasource = new CommentsDataSource(this);
        final ListView lv = (ListView) findViewById(android.R.id.list);
        String[] strArray = getResources().getStringArray(R.array.start_activity_list);

        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, strArray);
        lv.setAdapter(aa);
        lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();

                if (position == 0) {
                    new DatePickerDialog(StartTabActivity.this, d,
                            datetime.get(Calendar.YEAR),
                            datetime.get(Calendar.MONTH),
                            datetime.get(Calendar.DAY_OF_MONTH)
                    ).show();
                }
                else if (position == 1) {
                    new TimePickerDialog(StartTabActivity.this, t,
                            datetime.get(Calendar.HOUR),
                            datetime.get(Calendar.MINUTE),
                            true
                    ).show();
                }
                else if (position >= 2 && position <= 5) {
                    AlertDialog.Builder alertBox = new AlertDialog.Builder(StartTabActivity.this);
                    alertBox.setTitle(selected);
                    final EditText input = new EditText(StartTabActivity.this);
                    switch (position) {
                        case 2:
                            input.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                            break;
                        default:
                            input.setInputType(InputType.TYPE_CLASS_NUMBER);
                            break;
                    }
                    alertBox.setView(input);
                    alertBox.setCancelable(false);
                    final int positionId = position;
                    alertBox.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                            switch (positionId) {
                                case 2:
                                    activityDuration = Double.parseDouble(input.getText().toString());
                                    break;
                                case 3:
                                    activityDistance = Integer.parseInt(input.getText().toString());
                                    break;
                                case 4:
                                    activityCalories = Integer.parseInt(input.getText().toString());
                                    break;
                                case 5:
                                    activityHeartRate = Integer.parseInt(input.getText().toString());
                                    break;
                            }
                            dialog.cancel();
                        }
                    });
                    alertBox.setNegativeButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertBox.create();

                    // show it
                    alertDialog.show();
                }
                else {
                    AlertDialog.Builder alertBox = new AlertDialog.Builder(StartTabActivity.this);
                    alertBox.setTitle(selected);
                    final EditText input = new EditText(StartTabActivity.this);
                    alertBox.setView(input);
                    alertBox.setCancelable(false);
                    alertBox.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                            comment =input.getText().toString();
                            dialog.cancel();
                        }
                    });
                    alertBox.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertBox.create();

                    // show it
                    alertDialog.show();
                }
            }
        });
    }
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int month, int day) {
            datetime.set(Calendar.YEAR, year);
            datetime.set(Calendar.MONTH, month);
            datetime.set(Calendar.DAY_OF_MONTH, day);
        }
    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hour, int minute) {
            datetime.set(Calendar.HOUR, hour);
            datetime.set(Calendar.MINUTE, minute);
        }
    };

    public void saveInput(View v) {
        datasource = new CommentsDataSource(this);
        long dateTime = datetime.getTimeInMillis();
        Intent intent = getIntent();
        String inputType = intent.getStringExtra(MainActivity.INPUT_TYPE);
        String activityType = intent.getStringExtra(MainActivity.INPUT_ACTIVITY);
        datasource.open();
        datasource.createComment(inputType, activityType, dateTime, activityDuration, activityDistance, activityCalories, activityHeartRate, comment);
        datasource.close();
        Intent newIntent = new Intent(this, MainActivity.class);
        startActivity(newIntent);
    }

    public void deleteHistory(View v) {
        datasource = new CommentsDataSource(this);
        datasource.open();
        datasource.deleteAllComments();
        datasource.close();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
