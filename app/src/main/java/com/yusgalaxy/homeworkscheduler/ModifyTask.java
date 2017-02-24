package com.yusgalaxy.homeworkscheduler;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
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
import java.util.Calendar;
import java.util.Locale;

public class ModifyTask extends AppCompatActivity {

    private DatePickerDialog fromDate, toDate;
    private SimpleDateFormat dateFormat;

    private long _id;
    private EditText taskname, description, startDate, endDate;
    private Button modifybtn;

    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_task);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        //Find View By IDs
        taskname = (EditText)findViewById(R.id.enter_task_name);
        description = (EditText)findViewById(R.id.enter_description);
        modifybtn = (Button)findViewById(R.id.modify);

        startDate = (EditText)findViewById(R.id.enter_start_date);
        startDate.setInputType(InputType.TYPE_NULL);
        startDate.requestFocus();

        endDate = (EditText)findViewById(R.id.enter_end_date);
        startDate.setInputType(InputType.TYPE_NULL);

        setDateTimeField();

        //Set Text to EditText fields
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String task = intent.getStringExtra("taskname");
        String desc = intent.getStringExtra("desc");
        String fromDate = intent.getStringExtra("startDate");
        String toDate = intent.getStringExtra("endDate");

        _id = Long.parseLong(id);

        taskname.setText(task);
        description.setText(desc);
        startDate.setText(fromDate);
        endDate.setText(toDate);

        modifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTask();
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
                        Intent i1 = new Intent(ModifyTask.this, MainActivity.class);
                        startActivity(i1);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    private void updateTask(){

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

        Log.d("YG", "Updating the values from the database...");
        db.updateTasks(_id, tasks);

        Toast.makeText(this, R.string.data_modify_success, Toast.LENGTH_SHORT).show();
        Intent i2 = new Intent(ModifyTask.this, MainActivity.class);
        startActivity(i2);

    }

    protected void deleteTask(){
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage(R.string.delete_task_msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    private void delete(){
        
        Log.d("YG", "Deleting the values from the database...");
        db.deleteTasks(_id);
        Toast.makeText(this, R.string.data_delete_success, Toast.LENGTH_SHORT).show();
        Intent i3 = new Intent(ModifyTask.this, MainActivity.class);
        startActivity(i3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_update_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                exitTask();
                return true;
            case R.id.modify:
                updateTask();
                return true;
            case R.id.delete:
                deleteTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
