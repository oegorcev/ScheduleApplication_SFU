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
import Utils.Parsers.GroupParser;
import Utils.Scheduler;
import adapters.ScheduleItemAdapter;
import model.Week;

public class StartScreen extends AppCompatActivity {

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void set_currentSchedule(Week _currentSchedule) {
        this._currentSchedule = _currentSchedule;
    }

    private Week _currentSchedule;
    private ListView listView1;
    ScheduleItemAdapter scheduleItemAdapter;
    Map<String, Integer> day_of_a_weak;

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        setTitle("КТбо4-8");

        listView1 = (ListView) findViewById(R.id.list1);
        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        GroupParser groupParser = new GroupParser(this);
        groupParser.execute("КТбо4-8", "1");

        day_of_a_weak = new HashMap<>();
        day_of_a_weak.put(Constants.MONDAY, 0);
        day_of_a_weak.put(Constants.TUESDAY, 1);
        day_of_a_weak.put(Constants.WEDNESDAY, 2);
        day_of_a_weak.put(Constants.THURSDAY, 3);
        day_of_a_weak.put(Constants.FRIDAY, 4);
        day_of_a_weak.put(Constants.SATURDAY, 5);
        day_of_a_weak.put(Constants.SUNDAY, 6);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                int pickedDay = day_of_a_weak.get(tabId);
                scheduleItemAdapter = new ScheduleItemAdapter(StartScreen.this, _currentSchedule.getWeek().get(pickedDay).get_classesBotWeek());
                listView1.setAdapter(scheduleItemAdapter);
                scheduleItemAdapter.notifyDataSetChanged();
            }});

       // renderScheduleData("КТбо4-8");
    }

    public void renderScheduleData(String name) {
        ScheduleTask scheduleTask = new ScheduleTask(name);
        scheduleTask.execute();
    }

    private class  ScheduleTask extends AsyncTask<String, Void, Week> {

        private String _group;

        public ScheduleTask(String group) {
            super();
            _group = group;
        }

        @Override
        protected void onPreExecute() {

            if(isNetworkAvailable())
            {
                Scheduler scheduler = new Scheduler(StartScreen.this,_group);
                scheduler.execute();
            }
        }

        @Override
        protected Week doInBackground(String... params) {

            if(_currentSchedule != null)
            {
                return  _currentSchedule;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Week week) {

            super.onPostExecute(week);

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

            Calendar newCal = new GregorianCalendar();
            newCal.set(1997, 2, 1, 0, 0, 0);
            newCal.setTime(newCal.getTime());
            int day = newCal.get(Calendar.DAY_OF_WEEK) - 1;
            listView1 = (ListView) findViewById(R.id.list1);

            scheduleItemAdapter = new ScheduleItemAdapter(StartScreen.this, _currentSchedule.getWeek().get(day).get_classesBotWeek());

            listView1.setAdapter(scheduleItemAdapter);
            tabHost.setCurrentTab(day);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

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