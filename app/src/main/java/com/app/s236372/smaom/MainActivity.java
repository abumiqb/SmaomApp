package com.app.s236372.smaom;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private DBhjelp dbhjelp;
    private ListView visliste;
    private ArrayAdapter<String> arrayAP;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbhjelp = new DBhjelp(this);
        visliste = (ListView)findViewById(R.id.liste);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        oppdater();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog();
            }
        });
    }

    private void oppdater()
    {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = dbhjelp.getReadableDatabase();
        Cursor cursor = db.query(DBinfo.Nyoppgave.TABELL,
                new String[]{DBinfo.Nyoppgave._ID, DBinfo.Nyoppgave.KOL},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(DBinfo.Nyoppgave.KOL);
            taskList.add(cursor.getString(idx));
        }
        if (arrayAP == null)
        {
            arrayAP = new ArrayAdapter<>(this,
                    R.layout.content_main,
                    R.id.textView,
                    taskList);
            visliste.setAdapter(arrayAP);
        } else {
            arrayAP.clear();
            arrayAP.addAll(taskList);
            arrayAP.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }

    public void ferdig(View view)
    {
        View parent=(View) view.getParent();
        TextView taskview = (TextView) parent.findViewById(R.id.textView);
        String task= String.valueOf(taskview.getText());
        SQLiteDatabase db = dbhjelp.getWritableDatabase();
        db.delete(DBinfo.Nyoppgave.TABELL,
                DBinfo.Nyoppgave.KOL + " = ?",
                new String[]{task});
        db.close();
        oppdater();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void dialog()
    {
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Legg til nytt gjøremål")
                .setMessage("Hva vil du gjøre")
                .setView(taskEditText)
                .setPositiveButton("Legg til", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String task = String.valueOf(taskEditText.getText());
                        SQLiteDatabase db = dbhjelp.getReadableDatabase();
                        ContentValues values=new ContentValues();
                        values.put(DBinfo.Nyoppgave.KOL,task);
                        db.insertWithOnConflict(DBinfo.Nyoppgave.TABELL,null,values,SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                        oppdater();
                    }
                })
                .setNegativeButton("Tilbake", null)
                .create();
        dialog.show();
    }
}
