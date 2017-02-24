package com.yusgalaxy.homeworkscheduler;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private Database db = new Database(this);
    private ListView listView;
    private SimpleCursorAdapter cursorAdapter;

    final String[] from = new String[] {
            Database._ID, Database.KEY_TASKNAME, Database.KEY_DESCRIPTION,
            Database.KEY_STARTDATE, Database.KEY_ENDDATE
    };

    final int[] to = new int[] {
            R.id.id, R.id.task_name, R.id.description, R.id.start_date, R.id.end_date
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NewTask.class);
                startActivity(i);
            }
        });

        listView = (ListView)findViewById(R.id.list);
        listView.setEmptyView(findViewById(R.id.empty_list));

        readSchedulerData();
    }

    private void readSchedulerData(){
        Cursor cursor = db.getTaskNames();

        cursorAdapter = new SimpleCursorAdapter(this, R.layout.activity_view_tasks, cursor, from, to, 0);
        cursorAdapter.notifyDataSetChanged();

        listView.setAdapter(cursorAdapter);

        //OnClickListeners
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTextView = (TextView)view.findViewById(R.id.id);
                TextView nameTextView = (TextView)view.findViewById(R.id.task_name);
                TextView descTextView = (TextView)view.findViewById(R.id.description);
                TextView startDateTextView = (TextView)view.findViewById(R.id.start_date);
                TextView endDateTextView = (TextView)view.findViewById(R.id.end_date);

                String id = idTextView.getText().toString();
                String taskName = nameTextView.getText().toString();
                String desc = descTextView.getText().toString();
                String fromDate = startDateTextView.getText().toString();
                String toDate = endDateTextView.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(), ModifyTask.class);
                modify_intent.putExtra("id", id);
                modify_intent.putExtra("taskname", taskName);
                modify_intent.putExtra("desc", desc);
                modify_intent.putExtra("startDate", fromDate);
                modify_intent.putExtra("endDate", toDate);

                startActivity(modify_intent);
            }
        });
    }

}
