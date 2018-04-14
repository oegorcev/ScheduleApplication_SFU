package com.example.mrnobody43.shedule_application;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Utils.Constants;
import data.DataBase.DataBaseMapper;

/**
 * Created by Mr.Nobody43 on 24.01.2018.
 */

public class ChangeSchedule extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_schedule);

        if(getSupportActionBar()!= null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setTitle(Constants.EMPTY_STRING);

        _dataBaseMapper = new DataBaseMapper(this);
        search = (AutoCompleteTextView) findViewById(R.id.autocomplete_schedule);
        Reader reader = new Reader();
        reader.execute();
    }

    private String deleteEndSpases(String str) {
        for(int iCnt = (int)str.length() - 1; iCnt >= 0; --iCnt) {
            if(str.charAt(iCnt) != Constants.SPACE) {
                str = str.substring(0, iCnt + 1);
                break;
            }
        }

        return str;
    }

    public void initList() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, _queryDb);
        search.setAdapter(adapter);
    }

    private class Reader extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            _queryDb = new ArrayList<String>();
            search.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String file = "queries";
            int resId = getApplicationContext().getResources().getIdentifier(file, "raw", getApplicationContext().getPackageName());
            InputStream inputStream = getApplicationContext().getResources().openRawResource(resId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 8192);
            try {
                String test;
                while (true) {
                    test = reader.readLine();
                    if (test == null)
                        break;
                    _queryDb.add(test);
                }
                inputStream.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            initList();
            search.setEnabled(true);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.change_schedule_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.changeSchedule:
                String str = search.getText().toString();
                str = deleteEndSpases(str);
                Constants.addQuery(_dataBaseMapper.getCurruntQuery());
                _dataBaseMapper.setNewQuery(str);
                finish();
                break;
            case 16908332:
                finish();
            default:
                break;
        }

        return true;
    }

    ArrayAdapter<String> adapter;
    AutoCompleteTextView search;
    private ArrayList<String> _queryDb;
    private DataBaseMapper _dataBaseMapper;
}
