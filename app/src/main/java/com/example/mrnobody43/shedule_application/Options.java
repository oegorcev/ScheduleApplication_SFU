package com.example.mrnobody43.shedule_application;

import android.content.ContentValues;
import android.database.Cursor;
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

        if(getSupportActionBar()!= null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        _myDb = new DataBaseHelper(this);
        _spinnerSemestr = (Spinner)findViewById(R.id.spinnerSemestr);
        _spinnerYear = (Spinner)findViewById(R.id.spinnerYears);

        ArrayAdapter<?> adapterYear =
                ArrayAdapter.createFromResource(this, R.array.years, android.R.layout.simple_spinner_item);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<?> adapterSemestr =
                ArrayAdapter.createFromResource(this, R.array.semestrs, android.R.layout.simple_spinner_item);
        adapterSemestr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        _spinnerYear.setAdapter(adapterYear);
        _spinnerSemestr.setAdapter(adapterSemestr);

        setSpinnerValue();

        _spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                Integer potok = Constants.FIRST_POTOK + selectedItemPosition;
                String s = potok.toString();

                _db = _myDb.getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put(DataBaseHelper.ID, Constants.YEARS_DB_ID);
                cv.put(DataBaseHelper.OPTION, s);

                int was = _db.update(DataBaseHelper.TABLE_NAME2, cv, DataBaseHelper.ID + " = ?", new String[] { Constants.YEARS_DB_ID });
                if(was == 0)
                {
                    _db.insert(DataBaseHelper.TABLE_NAME2, null, cv);
                }

                _myDb.close();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        _spinnerSemestr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                String[] choose = getResources().getStringArray(R.array.semestrs);

                String s = choose[selectedItemPosition];

                _db = _myDb.getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put(DataBaseHelper.ID, Constants.SEMESTR_DB_ID);
                cv.put(DataBaseHelper.OPTION, s);

                int was = _db.update(DataBaseHelper.TABLE_NAME2, cv, DataBaseHelper.ID + " = ?", new String[] { Constants.SEMESTR_DB_ID });
                if(was == 0)
                {
                    _db.insert(DataBaseHelper.TABLE_NAME2, null, cv);
                }

                _myDb.close();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setSpinnerValue(){
        String compareValue = "some value";

        String semestr = "1";
        String potok = Constants.FIRST_POTOK.toString();

        _db = _myDb.getReadableDatabase();

        Cursor c = _db.query(DataBaseHelper.TABLE_NAME2, null, null, null, null, null, null);

        if (c.moveToFirst()) {

            while (true) {
                if (c.isAfterLast()) break;

                int idIndex = c.getColumnIndex(DataBaseHelper.ID);
                int optionIndex = c.getColumnIndex(DataBaseHelper.OPTION);

                String offlineData = c.getString(optionIndex);
                String bdId = c.getString(idIndex);

                if (Constants.SEMESTR_DB_ID.equals(bdId)) {
                    semestr = offlineData;
                } else if (Constants.YEARS_DB_ID.equals(bdId)) {
                    potok = offlineData;
                }

                c.moveToNext();

            }
        }
        _myDb.close();

        Integer semestrPos = Integer.parseInt(semestr) - 1;
        Integer yearPos = Integer.parseInt(potok) - Constants.FIRST_POTOK;

        _spinnerSemestr.setSelection(semestrPos);
        _spinnerYear.setSelection(yearPos);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private String _curSem;
    private String _curYear;
    private Spinner _spinnerSemestr;
    private Spinner _spinnerYear;
    private DataBaseHelper _myDb;
    private SQLiteDatabase _db;
}
