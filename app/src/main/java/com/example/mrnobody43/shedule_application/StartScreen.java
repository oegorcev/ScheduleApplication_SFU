package com.example.mrnobody43.shedule_application;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import Utils.Constants;
import Utils.Scheduler;
import adapters.ScheduleItemAdapter;
import model.Group.WeekGroup;
import model.Teacher.WeekTeacher;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        //setTitle("КТбо4-8");

        InitTabHost();

        renderScheduleData("Пирская Л.В.");
    }

    private class  ScheduleTask extends AsyncTask<String, Void, WeekGroup> {

        private String _group;

        public ScheduleTask(String group) {
            super();
            _group = group;
        }

        @Override
        protected void onPreExecute() {
            Scheduler scheduler = new Scheduler(StartScreen.this,_group);
            scheduler.execute();
        }

        @Override
        protected WeekGroup doInBackground(String... params) {

            if(_currentScheduleGroup != null)
            {
                return  _currentScheduleGroup;
            }

            return null;
        }

        @Override
        protected void onPostExecute(WeekGroup weekGroup) {

            super.onPostExecute(weekGroup);

            FillTabHost();

            Calendar newCal = new GregorianCalendar();
            int day = newCal.get(Calendar.DAY_OF_WEEK) - 1;

            if(_currentScheduleGroup!= null){
                scheduleItemAdapter = new ScheduleItemAdapter(StartScreen.this, _currentScheduleGroup.getWeek().get(day).get_classesBotWeek());
                listView1.setAdapter(scheduleItemAdapter);
            }

            tabHost.setCurrentTab(day);
        }
    }

    public void renderScheduleData(String name) {
        ScheduleTask scheduleTask = new ScheduleTask(name);
        scheduleTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void InitTabHost()
    {
        listView1 = (ListView) findViewById(R.id.list1);
        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        _day_of_a_weak = new HashMap<>();
        _day_of_a_weak.put(Constants.MONDAY, 0);
        _day_of_a_weak.put(Constants.TUESDAY, 1);
        _day_of_a_weak.put(Constants.WEDNESDAY, 2);
        _day_of_a_weak.put(Constants.THURSDAY, 3);
        _day_of_a_weak.put(Constants.FRIDAY, 4);
        _day_of_a_weak.put(Constants.SATURDAY, 5);
        _day_of_a_weak.put(Constants.SUNDAY, 6);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                int pickedDay = _day_of_a_weak.get(tabId);
                if(_currentScheduleGroup!= null) {
                    scheduleItemAdapter = new ScheduleItemAdapter(StartScreen.this, _currentScheduleGroup.getWeek().get(pickedDay).get_classesBotWeek());
                    listView1.setAdapter(scheduleItemAdapter);
                    scheduleItemAdapter.notifyDataSetChanged();
                }
            }});
    }

    private void FillTabHost()
    {
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(Constants.MONDAY);
        tabSpec.setContent(R.id.list1);
        tabSpec.setIndicator(Constants.MONDAY);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(Constants.TUESDAY);
        tabSpec.setContent(R.id.list1);
        tabSpec.setIndicator(Constants.TUESDAY);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(Constants.WEDNESDAY);
        tabSpec.setContent(R.id.list1);
        tabSpec.setIndicator(Constants.WEDNESDAY);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(Constants.THURSDAY);
        tabSpec.setContent(R.id.list1);
        tabSpec.setIndicator(Constants.THURSDAY);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(Constants.FRIDAY);
        tabSpec.setContent(R.id.list1);
        tabSpec.setIndicator(Constants.FRIDAY);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(Constants.SATURDAY);
        tabSpec.setContent(R.id.list1);
        tabSpec.setIndicator(Constants.SATURDAY);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(Constants.SUNDAY);
        tabSpec.setContent(R.id.list1);
        tabSpec.setIndicator(Constants.SUNDAY);
        tabHost.addTab(tabSpec);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void set_currentSchedule(WeekGroup _currentSchedule) {
        this._currentScheduleGroup = _currentSchedule;
    }

    public void set_currentSchedule(WeekTeacher _currentSchedule) {
        this._currentScheduleTeacher = _currentSchedule;
    }


    private WeekGroup _currentScheduleGroup;
    private WeekTeacher _currentScheduleTeacher;
    private ListView listView1;
    private TabHost tabHost;
    ScheduleItemAdapter scheduleItemAdapter;
    private Map<String, Integer> _day_of_a_weak;
}
//listView.setOnItemClickListener(new OnItemClickListener() {
//@Override
//public void onItemClick(AdapterView<?> parent, View view,
//        int position, long id) {
//        Toast.makeText(getApplicationContext(),
//        "Click ListItem Number " + position, Toast.LENGTH_LONG)
//        .show();
//        }
//        })