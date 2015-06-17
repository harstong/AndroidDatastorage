package com.example.greg.androiddatastorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getPreferences(0);
        TextView t = (TextView) findViewById(R.id.Count);
        t.setText(Integer.toString(settings.getInt("count", 0)));
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

        SharedPreferences.Editor settings = getPreferences(0).edit();

        settings.putInt("count", value);
        settings.commit();
    }

}

class DataBase extends SQLiteOpenHelper {
    private static final String NAME = "DataBase";
    private static final int VERSION = 2;

    public DataBase(Context context) {
        super(context, NAME, null, VERSION);
        
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
