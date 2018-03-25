package com.example.mrnobody43.shedule_application;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Смена расписания");

        _dataBaseMapper = new DataBaseMapper(this);
        changeSchedule = (Button) findViewById(R.id.changeSchedule);
        search = (EditText) findViewById(R.id.search);
        searchList = (ListView) findViewById(R.id.searchList);
        Reader reader = new Reader();
        reader.execute();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.toString().equals("")) {
                    if(_currentQueryDb != null)_currentQueryDb.clear();
                } else {
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        changeSchedule.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                String str = search.getText().toString();
                str = deleteEndSpases(str);
                Constants.addQuery(_dataBaseMapper.getCurruntQuery());
                _dataBaseMapper.setNewQuery(str);

                finish();
            }
        });

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


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void initList() {
        _currentQueryDb = new ArrayList<>(_queryDb);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, _currentQueryDb);
        searchList.setAdapter(adapter);

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                TextView textView = (TextView) itemClicked;
                String strText = textView.getText().toString();

                Intent intent = new Intent();

                search.setText(strText);
            }
        });
    }

    private void searchItem(String text) {
        if(_currentQueryDb != null) _currentQueryDb.clear();
        text = text.toLowerCase();
        for (String item : _queryDb) {
            String lowerItem = item.toLowerCase();
            if (lowerItem.contains(text)) {
                _currentQueryDb.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    class Reader extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            _queryDb = new ArrayList<String>();
            search.setEnabled(false);
            changeSchedule.setVisibility(View.INVISIBLE);
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
            changeSchedule.setVisibility(View.VISIBLE);
            search.setEnabled(true);
        }
    }

    ListView searchList;
    Button changeSchedule;
    ArrayAdapter<String> adapter;
    EditText search;
    private ArrayList<String> _queryDb;
    private ArrayList<String> _currentQueryDb;
    private DataBaseMapper _dataBaseMapper;
}
