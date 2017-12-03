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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private DBhjelp dbhjelp;
    private ListView visliste;
    private ImageView logo;
    private TextView tekst;
    private ArrayAdapter<String> arrayAP;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbhjelp = new DBhjelp(this);
        visliste = (ListView)findViewById(R.id.liste);
        logo = (ImageView)findViewById(R.id.logo);
        tekst = (TextView)findViewById(R.id.textView2);
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
        ArrayList<String> liste = new ArrayList<>();
        SQLiteDatabase db = dbhjelp.getReadableDatabase();
        Cursor cursor = db.query(DBinfo.Nyoppgave.TABELL, new String[]{DBinfo.Nyoppgave._ID, DBinfo.Nyoppgave.KOL}, null, null, null, null, null);

        while(cursor.moveToNext())
        {
            int idx = cursor.getColumnIndex(DBinfo.Nyoppgave.KOL);
            liste.add(cursor.getString(idx));
        }
        if (arrayAP == null)
        {
            arrayAP = new ArrayAdapter<>(this, R.layout.content_main, R.id.tekstElement, liste);
            visliste.setAdapter(arrayAP);
        }
        else
        {
            arrayAP.clear();
            arrayAP.addAll(liste);
            arrayAP.notifyDataSetChanged();
        }

        if (arrayAP.isEmpty())
        {
            logo.setVisibility(View.VISIBLE);
            tekst.setVisibility(View.VISIBLE);
        }

        if (!arrayAP.isEmpty())
        {
            logo.setVisibility(View.INVISIBLE);
            tekst.setVisibility(View.INVISIBLE);
        }

        cursor.close();
            db.close();
    }

    public void ferdig(View view)
    {
        View parent=(View) view.getParent();
        TextView taskview = (TextView) parent.findViewById(R.id.tekstElement);
        String oppg = String.valueOf(taskview.getText());
        SQLiteDatabase db = dbhjelp.getWritableDatabase();
        db.delete(DBinfo.Nyoppgave.TABELL, DBinfo.Nyoppgave.KOL + " = ?", new String[]{oppg});
        oppdater();
        db.close();
    }

    public void update(View view)
    {
        View parent=(View) view.getParent();
        final TextView taskview = (TextView) parent.findViewById(R.id.tekstElement);
        final String oppg = String.valueOf(taskview.getText());

        final EditText tekst = new EditText(this);
        tekst.setText(taskview.getText());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Endre på gjøremål")
                .setMessage("Hva vil du gjøre?")
                .setView(tekst)

                .setPositiveButton("Oppdater", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        SQLiteDatabase db = dbhjelp.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        String nyVerdi = tekst.getText().toString();
                        values.put("Tittel", nyVerdi);
                        taskview.setText(nyVerdi);
                        System.out.println("Nyverdi: " + nyVerdi);
                        db.update(DBinfo.Nyoppgave.TABELL, values, DBinfo.Nyoppgave.KOL + " = ?", new String[]{oppg});
                        oppdater();
                        db.close();
                    }
                })
                .setNegativeButton("Tilbake", null)
                .create();
        dialog.show();
    }

    public void dialog()
    {
        final EditText tekst = new EditText(this);
        final EditText tekst1 = new EditText(this);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Legg til nytt gjøremål")
                .setMessage("Hva vil du gjøre")
                .setView(tekst)

                .setPositiveButton("Legg til", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String oppgave = String.valueOf(tekst.getText());
                        SQLiteDatabase db = dbhjelp.getReadableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(DBinfo.Nyoppgave.KOL,oppgave);
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
