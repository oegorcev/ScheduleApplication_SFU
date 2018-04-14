package com.example.mrnobody43.shedule_application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
        hideWeeks = (CheckBox)findViewById(R.id.hideWeeksCheckbox);
        saveChanges = (Button) findViewById(R.id.saveButton) ;

        ArrayAdapter<?> adapterYear =
                ArrayAdapter.createFromResource(this, R.array.years, android.R.layout.simple_spinner_item);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<?> adapterSemestr =
                ArrayAdapter.createFromResource(this, R.array.semestrs, android.R.layout.simple_spinner_item);
        adapterSemestr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinnerYear.setAdapter(adapterYear);
        spinnerSemestr.setAdapter(adapterSemestr);

        setSpinnerValue();
        setHideWeeks();

        saveChanges.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                _dataBaseMapper.setPotok(_potok);
                _dataBaseMapper.setSemestr(_semestr);
                _dataBaseMapper.setHideWeeks(_hideWeek);

                finish();
            }
        });

        hideWeeks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(hideWeeks.isChecked()) {
                    _hideWeek = "1";
                }
                else {
                    _hideWeek ="0";
                }
            }

        });

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                Integer potok = Constants.FIRST_POTOK + selectedItemPosition;
                _potok = potok.toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerSemestr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                String[] choose = getResources().getStringArray(R.array.semestrs);
                _semestr = choose[selectedItemPosition];
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setSpinnerValue(){

        ArrayList<Integer> params = _dataBaseMapper.getSpinnerParams();

        _potok = Integer.toString(Constants.FIRST_POTOK + params.get(1));
        _semestr = params.get(0).toString();

        spinnerSemestr.setSelection(params.get(0));
        spinnerYear.setSelection(params.get(1));
    }

    private void setHideWeeks(){
        Integer flag = _dataBaseMapper.getHideWeeksOption();

        if(flag == 1) {
            _hideWeek = "1";
            hideWeeks.setChecked(true);
        } else {
            _hideWeek = "0";
            hideWeeks.setChecked(false);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    CheckBox hideWeeks;
    Button saveChanges;
    Spinner spinnerSemestr;
    Spinner spinnerYear;
    private String _potok;
    private String _hideWeek;
    private String _semestr;
    private DataBaseMapper _dataBaseMapper;
}
