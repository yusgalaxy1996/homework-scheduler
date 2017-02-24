package com.yusgalaxy.homeworkscheduler;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Database extends SQLiteOpenHelper {

    private static final String DB_NAME = "tasklist.db";
    private static final String TABLE_NAME = "tasks";

    public static final String _ID = "_id";
    public static final String KEY_TASKNAME = "task_name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_STARTDATE = "start_date";
    public static final String KEY_ENDDATE = "end_date";

    public Database(Context context){
        super(context, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = String.format("CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TASKNAME + " TEXT, " + KEY_DESCRIPTION + " TEXT, " + KEY_STARTDATE + " DATE, " +
                KEY_ENDDATE + " DATE)");
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Add new task
    public void storeTasks(Tasks tasks){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASKNAME, tasks.getName());
        Log.d("YG", "Taskname Key inserted to table: " + tasks.getName());
        values.put(KEY_DESCRIPTION, tasks.getDescription());
        Log.d("YG", "Description Key inserted to table: " + tasks.getDescription());
        values.put(KEY_STARTDATE, tasks.getFromDate());
        Log.d("YG", "Startdate Key inserted to table: " + tasks.getFromDate());
        values.put(KEY_ENDDATE, tasks.getToDate());
        Log.d("YG", "Enddate Key inserted to table: " + tasks.getToDate());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //List all task names
    public Cursor getTaskNames(){
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = new String[] {
                _ID, KEY_TASKNAME, KEY_DESCRIPTION, KEY_STARTDATE, KEY_ENDDATE
        };
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    //Update the task
    public void updateTasks(long _id, Tasks tasks){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASKNAME, tasks.getName());
        Log.d("YG", "Taskname Key updated to table: " + tasks.getName());
        values.put(KEY_DESCRIPTION, tasks.getDescription());
        Log.d("YG", "Description Key updated to table: " + tasks.getDescription());
        values.put(KEY_STARTDATE, tasks.getFromDate());
        Log.d("YG", "Startdate Key updated to table: " + tasks.getFromDate());
        values.put(KEY_ENDDATE, tasks.getToDate());
        Log.d("YG", "Enddate Key updated to table: " + tasks.getToDate());

        db.update(TABLE_NAME, values, _ID + " = " + _id, null);
        db.close();
    }

    //Delete a task
    public void deleteTasks(long _id){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_NAME, _ID + " = " + _id, null);
        db.close();
    }
}
