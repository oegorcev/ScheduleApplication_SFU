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

import Utils.Constants;
import Utils.Scheduler;
import fragments.ScheduleItemAdapter;
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
    private ListView listView2;
    private ListView listView3;
    private ListView listView4;
    private ListView listView5;
    private ListView listView6;
    private ListView listView7;
    ScheduleItemAdapter scheduleItemAdapter;

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        setTitle("КТбо4-8");

        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        renderScheduleData("КТбо4-8");
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

            TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");
            tabSpec.setContent(R.id.list1);
            tabSpec.setIndicator(Constants.MONDAY);
            tabHost.addTab(tabSpec);

            tabSpec = tabHost.newTabSpec("tag2");
            tabSpec.setContent(R.id.list2);
            tabSpec.setIndicator(Constants.TUESDAY);
            tabHost.addTab(tabSpec);

            tabSpec = tabHost.newTabSpec("tag3");
            tabSpec.setContent(R.id.list3);
            tabSpec.setIndicator(Constants.WEDNESDAY);
            tabHost.addTab(tabSpec);

            tabSpec = tabHost.newTabSpec("tag4");
            tabSpec.setContent(R.id.list4);
            tabSpec.setIndicator(Constants.THURSDAY);
            tabHost.addTab(tabSpec);

            tabSpec = tabHost.newTabSpec("tag5");
            tabSpec.setContent(R.id.list5);
            tabSpec.setIndicator(Constants.FRIDAY);
            tabHost.addTab(tabSpec);

            tabSpec = tabHost.newTabSpec("tag6");
            tabSpec.setContent(R.id.list6);
            tabSpec.setIndicator(Constants.SATURDAY);
            tabHost.addTab(tabSpec);

            tabSpec = tabHost.newTabSpec("tag7");
            tabSpec.setContent(R.id.list7);
            tabSpec.setIndicator(Constants.SUNDAY);
            tabHost.addTab(tabSpec);

            tabHost.setCurrentTab(0);
            
            listView1 = (ListView) findViewById(R.id.list1);

            scheduleItemAdapter = new ScheduleItemAdapter(StartScreen.this, _currentSchedule.getWeek().get(0).get_classesBotWeek());

            // создаем адаптер
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(StartScreen.this, android.R.layout.simple_list_item_1, names);
            listView1.setAdapter(scheduleItemAdapter);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
