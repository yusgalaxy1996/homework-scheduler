package com.yusgalaxy.homeworkscheduler;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewTask extends AppCompatActivity {

    private DatePickerDialog fromDate, toDate;
    private SimpleDateFormat dateFormat;
    private EditText taskname, description, startDate, endDate;
    private Button submitbtn;

    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        //Find View By IDs
        taskname = (EditText)findViewById(R.id.enter_task_name);
        description = (EditText)findViewById(R.id.enter_description);
        submitbtn = (Button)findViewById(R.id.submit);

        startDate = (EditText)findViewById(R.id.enter_start_date);
        startDate.setInputType(InputType.TYPE_NULL);
        startDate.requestFocus();

        endDate = (EditText)findViewById(R.id.enter_end_date);
        endDate.setInputType(InputType.TYPE_NULL);

        setDateTimeField();

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitTask();
            }
        });
    }

    private void setDateTimeField(){
        Calendar newCalendar = Calendar.getInstance();
        fromDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(i, i1, i2);
                startDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(i, i1, i2);
                endDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDate.show();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDate.show();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            exitTask();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitTask(){
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage(R.string.new_task_exit)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent i1 = new Intent(NewTask.this, MainActivity.class);
                        startActivity(i1);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    private void submitTask(){

        String taskName = taskname.getText().toString();
        String desc = description.getText().toString();
        String startDate1 = startDate.getText().toString();
        String endDate1 = endDate.getText().toString();

        if(TextUtils.isEmpty(taskName)){
            taskname.setError("Please enter the homework/task name!");
            return;
        }

        if(TextUtils.isEmpty(startDate1)){
            startDate.setError("Please enter the starting date!");
            return;
        }

        if(TextUtils.isEmpty(endDate1)){
            endDate.setError("Please enter the ending date!");
            return;
        }


        Tasks tasks = new Tasks(taskName, desc, startDate1, endDate1);

        Log.d("YG", "Inserting the values to the database...");
        db.storeTasks(tasks);

        Toast.makeText(this, R.string.data_insert_success, Toast.LENGTH_SHORT).show();
        Intent i2 = new Intent(NewTask.this, MainActivity.class);
        startActivity(i2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_new_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                exitTask();
                return true;
            case R.id.submit:
                submitTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
