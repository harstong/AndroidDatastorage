package com.example.greg.androiddatastorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private CountDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int count = 0;
        dataSource = new CountDataSource(this);
        try {
            dataSource.open();
            List<Integer> counts = dataSource.getAllCounts();
            if (counts.size() > 0) {
                count = counts.get(0);
            }
            dataSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TextView t = (TextView) findViewById(R.id.Count);
        t.setText(Integer.toString(count));
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

    public void Advance(View view) {
        TextView t = (TextView) findViewById(R.id.Count);
        int value = Integer.parseInt(String.valueOf(t.getText()));

        t.setText(Integer.toString(value + 1));
    }

    public void SaveContent(View view) {
        TextView t = (TextView) findViewById(R.id.Count);
        int value = Integer.parseInt(String.valueOf(t.getText()));

        try {
            dataSource.open();
            dataSource.replaceCount(0, value);
            dataSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class CountDataSource {
    private SQLiteDatabase database;
    private MyDatabaseHelper dbHelper;
    private String[] allColumns = { MyDatabaseHelper.COLUMN_ID, MyDatabaseHelper.COLUMN_COUNT };

    public CountDataSource(Context context) {
        dbHelper = new MyDatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long storeCount(int count) {
        ContentValues values = new ContentValues();
        values.put(MyDatabaseHelper.COLUMN_COUNT, count);
        return database.insert(MyDatabaseHelper.TABLE_COUNTS, null, values);
    }

    public void deleteCount(long rowId) {
        database.delete(MyDatabaseHelper.TABLE_COUNTS,
                MyDatabaseHelper.COLUMN_ID + " = " + rowId, null);
    }

    public void replaceCount(long rowId, int value) {
        ContentValues values = new ContentValues();
        values.put(MyDatabaseHelper.COLUMN_ID, 0);
        values.put(MyDatabaseHelper.COLUMN_COUNT, value);
        database.replace(MyDatabaseHelper.TABLE_COUNTS, null, values);
    }

    public int getCount(int rowId) {
        Cursor cursor = database.query(MyDatabaseHelper.TABLE_COUNTS, allColumns,
                MyDatabaseHelper.COLUMN_ID + " = " + rowId, null, null, null, null);
        cursor.moveToFirst();
        int count = cursor2count(cursor);
        cursor.close();
        return count;
    }

    public List<Integer> getAllCounts() {
        List<Integer> counts = new ArrayList<>();
        Cursor cursor = database.query(MyDatabaseHelper.TABLE_COUNTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            counts.add(cursor.getInt(1));
            cursor.moveToNext();
        }

        cursor.close();
        return counts;
    }

    private int cursor2count(Cursor cursor) {
        return cursor.getInt(1);
    }
}

class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_COUNTS = "counts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COUNT = "count";

    public static final String DATABASE_NAME = "count.db";
    public static final int VERSION = 1;

    private static final String DATABASE_CREATE = "create table " + TABLE_COUNTS + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_COUNT + " text not null);";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MyDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", destroying all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTS);
        onCreate(db);
    }
}
