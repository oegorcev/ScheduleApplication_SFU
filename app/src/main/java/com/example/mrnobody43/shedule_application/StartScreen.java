package com.example.mrnobody43.shedule_application;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import Utils.Constants;
import Utils.Scheduler;
import Utils.Utilities;
import adapters.ScheduleClassRoomAdapter;
import adapters.ScheduleEmptyAdapter;
import adapters.ScheduleGroupAdapter;
import adapters.ScheduleTeacherAdapter;
import model.ClassRoom.WeekClassRoom;
import model.Group.WeekGroup;
import model.Teacher.WeekTeacher;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        if(_query.equals(Constants.EMPTY_STRING)) _query= "Сапунцов Н.Е.";

        _CURRENT_STATE = Utilities.SetState(_query);

        InitTabHost();

        renderScheduleData(_query);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            _query = data.getStringExtra(Constants.CHANGED_SCHEDULE);
            renderScheduleData(_query);
        }
    }

    public void renderScheduleData(String name) {
        _CURRENT_STATE = Utilities.SetState(name);
        _query = Utilities.CheckSpecialsCases(name);
        setTitle(_query);
        ScheduleTask scheduleTask = new ScheduleTask(_query);
        scheduleTask.execute();
    }

    private class  ScheduleTask extends AsyncTask<String, Void, Void> {

        private String _current_query;

        public ScheduleTask(String current_query) {
            super();
            _query = current_query;
            _current_query = current_query;
        }

        @Override
        protected void onPreExecute() {
            Scheduler scheduler = new Scheduler(StartScreen.this,_current_query);
            scheduler.execute();
        }

        @Override
        protected Void doInBackground(String... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            FillTabHost();

            Calendar newCal = new GregorianCalendar();
            int day = newCal.get(Calendar.DAY_OF_WEEK) - 2;
            if(day < 0){
                day = Constants.DAYS_ON_WEEK - 1;
            }

            switch (_CURRENT_STATE) {
                case Constants.GROUP: {
                    if(_currentScheduleGroup!= null){
                        scheduleGroupAdapter  = new ScheduleGroupAdapter(StartScreen.this, _currentScheduleGroup.getWeek().get(day).get_classesBotWeek());
                        listView1.setAdapter(scheduleGroupAdapter);
                    }
                    else {
                        listView1.setAdapter(new ScheduleEmptyAdapter(StartScreen.this));
                    }
                    break;
                }
                case Constants.TEACHER: {
                    if(_currentScheduleTeacher!= null) {
                        scheduleTeacherAdapter = new ScheduleTeacherAdapter(StartScreen.this, _currentScheduleTeacher.getWeek().get(day).get_classesBotWeek());
                        listView1.setAdapter(scheduleTeacherAdapter);
                    }
                    else {
                        listView1.setAdapter(new ScheduleEmptyAdapter(StartScreen.this));
                    }
                    break;
                }
                case Constants.CLASSROOM: {
                    if(_currentScheduleClassRoom!= null){
                        scheduleClassRoomAdapter  = new ScheduleClassRoomAdapter(StartScreen.this, _currentScheduleClassRoom.getWeek().get(day).get_classesBotWeek());
                        listView1.setAdapter(scheduleClassRoomAdapter);
                    }
                    else {
                        listView1.setAdapter(new ScheduleEmptyAdapter(StartScreen.this));
                    }
                    break;
                }
                default: {
                    break;
                }
            }

            tabHost.setCurrentTab(day);
        }
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

                switch (_CURRENT_STATE) {
                    case Constants.GROUP: {
                        if(_currentScheduleGroup!= null){
                            scheduleGroupAdapter  = new ScheduleGroupAdapter(StartScreen.this, _currentScheduleGroup.getWeek().get(pickedDay).get_classesBotWeek());
                            listView1.setAdapter(scheduleGroupAdapter);
                            scheduleGroupAdapter.notifyDataSetChanged();
                        }
                        else {
                            listView1.setAdapter(new ScheduleEmptyAdapter(StartScreen.this));
                        }
                        break;
                    }
                    case Constants.TEACHER: {
                        if(_currentScheduleTeacher!= null) {
                            scheduleTeacherAdapter = new ScheduleTeacherAdapter(StartScreen.this, _currentScheduleTeacher.getWeek().get(pickedDay).get_classesBotWeek());
                            listView1.setAdapter(scheduleTeacherAdapter);
                            scheduleTeacherAdapter.notifyDataSetChanged();
                        }
                        else {
                            listView1.setAdapter(new ScheduleEmptyAdapter(StartScreen.this));
                        }
                        break;
                    }
                    case Constants.CLASSROOM: {
                        if(_currentScheduleClassRoom!= null){
                            scheduleClassRoomAdapter  = new ScheduleClassRoomAdapter(StartScreen.this, _currentScheduleClassRoom.getWeek().get(pickedDay).get_classesBotWeek());
                            listView1.setAdapter(scheduleClassRoomAdapter);
                            scheduleClassRoomAdapter.notifyDataSetChanged();
                        }
                        else {
                            listView1.setAdapter(new ScheduleEmptyAdapter(StartScreen.this));
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }});


        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if(_CURRENT_STATE == Constants.GROUP) {
                    _prev_query = _query;
                    TextView textView = (TextView) view.findViewById(R.id.teacher1);
                    _query = textView.getText().toString();

                    renderScheduleData(textView.getText().toString());
                }

            }
        });
    }

    private void FillTabHost()
    {
        if(tabHost.getTabWidget().getTabCount() != Constants.DAYS_ON_WEEK){
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

    public void set_currentSchedule(WeekClassRoom _currentSchedule) {
        this._currentScheduleClassRoom = _currentSchedule;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.changeSchedule:
                Intent intent = new Intent(StartScreen.this, ChangeSchedule.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.updateSchedule:
                renderScheduleData(_query);
                break;
            case R.id.examsSchedule:

                break;
            default:
                break;
        }

        return true;
    }

    private WeekGroup _currentScheduleGroup;
    private WeekTeacher _currentScheduleTeacher;
    private WeekClassRoom _currentScheduleClassRoom;
    private ListView listView1;
    private String _prev_query;
    private String _query = "";
    private TabHost tabHost;
    private Integer _CURRENT_STATE;
    ScheduleGroupAdapter scheduleGroupAdapter;
    ScheduleClassRoomAdapter scheduleClassRoomAdapter;
    ScheduleTeacherAdapter scheduleTeacherAdapter;
    private Map<String, Integer> _day_of_a_weak;
}
