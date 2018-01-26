package com.example.mrnobody43.shedule_application;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import Utils.Constants;
import data.DataBaseHelper;

/**
 * Created by Mr.Nobody43 on 26.01.2018.
 */

public class Options extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        myDb = new DataBaseHelper(this);
        spinnerSemestr = (Spinner)findViewById(R.id.spinnerSemestr);
        spinnerYear = (Spinner)findViewById(R.id.spinnerYears);

        ArrayAdapter<?> adapterYear =
                ArrayAdapter.createFromResource(this, R.array.years, android.R.layout.simple_spinner_item);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<?> adapterSemestr =
                ArrayAdapter.createFromResource(this, R.array.semestrs, android.R.layout.simple_spinner_item);
        adapterSemestr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinnerYear.setAdapter(adapterYear);
        spinnerSemestr.setAdapter(adapterSemestr);

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                String[] choose = getResources().getStringArray(R.array.years);
                Integer potok = Constants.FIRST_POTOK + selectedItemPosition;
                String s = potok.toString();

                db = myDb.getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put(DataBaseHelper.ID, Constants.YEARS_DB_ID);
                cv.put(DataBaseHelper.OPTION, s);

                int was = db.update(DataBaseHelper.TABLE_NAME2, cv, DataBaseHelper.ID + " = ?", new String[] { Constants.YEARS_DB_ID });
                if(was == 0)
                {
                    db.insert(DataBaseHelper.TABLE_NAME2, null, cv);
                }

                myDb.close();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerSemestr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                String[] choose = getResources().getStringArray(R.array.semestrs);

                String s = choose[selectedItemPosition];

                db = myDb.getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put(DataBaseHelper.ID, Constants.SEMESTR_DB_ID);
                cv.put(DataBaseHelper.OPTION, s);

                int was = db.update(DataBaseHelper.TABLE_NAME2, cv, DataBaseHelper.ID + " = ?", new String[] { Constants.SEMESTR_DB_ID });
                if(was == 0)
                {
                    db.insert(DataBaseHelper.TABLE_NAME2, null, cv);
                }

                myDb.close();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    Spinner spinnerSemestr;
    Spinner spinnerYear;
    private DataBaseHelper myDb;
    private SQLiteDatabase db;
}
