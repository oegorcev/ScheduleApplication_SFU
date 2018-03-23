package com.example.mrnobody43.shedule_application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import Utils.Constants;
import data.DataBase.DataBaseMapper;

/**
 * Created by Mr.Nobody43 on 26.01.2018.
 */

public class Options extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Настройки");
        setContentView(R.layout.activity_options);

        _dataBaseMapper = new DataBaseMapper(this);

        if(getSupportActionBar()!= null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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

        setSpinnerValue();

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                Integer potok = Constants.FIRST_POTOK + selectedItemPosition;
                _dataBaseMapper.setPotok(potok.toString());
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerSemestr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                String[] choose = getResources().getStringArray(R.array.semestrs);
                _dataBaseMapper.setSemestr(choose[selectedItemPosition]);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setSpinnerValue(){

        ArrayList<Integer> params = _dataBaseMapper.getSpinnerParams();

        spinnerSemestr.setSelection(params.get(0));
        spinnerYear.setSelection(params.get(1));
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    Spinner spinnerSemestr;
    Spinner spinnerYear;
    private DataBaseMapper _dataBaseMapper;
}
