package com.example.mrnobody43.shedule_application;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import Utils.Constants;
import Utils.Utilities;
import adapters.ScheduleClassRoomAdapter;
import adapters.ScheduleEmptyAdapter;
import adapters.ScheduleGroupAdapter;
import adapters.ScheduleTeacherAdapter;
import data.DataBaseHelper;
import data.Scheduler;
import model.ClassRoom.WeekClassRoom;
import model.Group.WeekGroup;
import model.Teacher.WeekTeacher;

public class MainSchedule extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        _myDb = new DataBaseHelper(this);

         _query= GetCurruntQuery();

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
        _query = name;
        setTitle(_query);

        _currentScheduleClassRoom = null;
        _currentScheduleGroup = null;
        _currentScheduleTeacher = null;

        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.execute();
    }

    private class  ScheduleTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            Scheduler scheduler = new Scheduler(MainSchedule.this,_query);
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
                        scheduleGroupAdapter  = new ScheduleGroupAdapter(MainSchedule.this, _currentScheduleGroup.getWeek().get(day).get_classesBotWeek());
                        listView1.setAdapter(scheduleGroupAdapter);
                    }
                    else {
                        listView1.setAdapter(new ScheduleEmptyAdapter(MainSchedule.this));
                    }
                    break;
                }
                case Constants.TEACHER: {
                    if(_currentScheduleTeacher!= null) {
                        scheduleTeacherAdapter = new ScheduleTeacherAdapter(MainSchedule.this, _currentScheduleTeacher.getWeek().get(day).get_classesBotWeek());
                        listView1.setAdapter(scheduleTeacherAdapter);
                    }
                    else {
                        listView1.setAdapter(new ScheduleEmptyAdapter(MainSchedule.this));
                    }
                    break;
                }
                case Constants.CLASSROOM: {
                    if(_currentScheduleClassRoom!= null){
                        scheduleClassRoomAdapter  = new ScheduleClassRoomAdapter(MainSchedule.this, _currentScheduleClassRoom.getWeek().get(day).get_classesBotWeek());
                        listView1.setAdapter(scheduleClassRoomAdapter);
                    }
                    else {
                        listView1.setAdapter(new ScheduleEmptyAdapter(MainSchedule.this));
                    }
                    break;
                }
                default: {
                    break;
                }
            }

            tabHost.setCurrentTab(day);

            _db = _myDb.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(DataBaseHelper.ID, Constants.CUR_QUERY_DB_ID);
            cv.put(DataBaseHelper.OPTION, _query);

            int was = _db.update(DataBaseHelper.TABLE_NAME2, cv, DataBaseHelper.ID + " = ?", new String[] { Constants.CUR_QUERY_DB_ID });
            if(was == 0)
            {
                _db.insert(DataBaseHelper.TABLE_NAME2, null, cv);
            }

            _myDb.close();
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
                            scheduleGroupAdapter  = new ScheduleGroupAdapter(MainSchedule.this, _currentScheduleGroup.getWeek().get(pickedDay).get_classesBotWeek());
                            listView1.setAdapter(scheduleGroupAdapter);
                            scheduleGroupAdapter.notifyDataSetChanged();
                        }
                        else {
                            listView1.setAdapter(new ScheduleEmptyAdapter(MainSchedule.this));
                        }
                        break;
                    }
                    case Constants.TEACHER: {
                        if(_currentScheduleTeacher!= null) {
                            scheduleTeacherAdapter = new ScheduleTeacherAdapter(MainSchedule.this, _currentScheduleTeacher.getWeek().get(pickedDay).get_classesBotWeek());
                            listView1.setAdapter(scheduleTeacherAdapter);
                            scheduleTeacherAdapter.notifyDataSetChanged();
                        }
                        else {
                            listView1.setAdapter(new ScheduleEmptyAdapter(MainSchedule.this));
                        }
                        break;
                    }
                    case Constants.CLASSROOM: {
                        if(_currentScheduleClassRoom!= null){
                            scheduleClassRoomAdapter  = new ScheduleClassRoomAdapter(MainSchedule.this, _currentScheduleClassRoom.getWeek().get(pickedDay).get_classesBotWeek());
                            listView1.setAdapter(scheduleClassRoomAdapter);
                            scheduleClassRoomAdapter.notifyDataSetChanged();
                        }
                        else {
                            listView1.setAdapter(new ScheduleEmptyAdapter(MainSchedule.this));
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }});
    }

    public void onTeacherClick(View V) {

        LinearLayout vwParentRow = (LinearLayout)V.getParent();

        _prev_query = _query;

        TextView child = (TextView)vwParentRow.getChildAt(1);
        String new_query = child.getText().toString();

        renderScheduleData(new_query);
    }


    public void onClassroomClick(View V) {

        LinearLayout vwParentRow = (LinearLayout)V.getParent();

        _prev_query = _query;

        TextView child = (TextView)vwParentRow.getChildAt(2);
        String new_query = child.getText().toString();

        renderScheduleData(new_query);
    }


    private String GetCurruntQuery()
    {
        String s = "Расписание занятий ИТА ЮФУ";

        _db = _myDb.getReadableDatabase();

        Cursor c = _db.query(DataBaseHelper.TABLE_NAME2, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            boolean flag = true;
            while (true) {
                if (c.isAfterLast()) break;

                int idIndex = c.getColumnIndex(DataBaseHelper.ID);
                int optionIndex = c.getColumnIndex(DataBaseHelper.OPTION);

                String offlineData = c.getString(optionIndex);
                String bdId = c.getString(idIndex);
                if (Constants.CUR_QUERY_DB_ID.equals(bdId)) {
                    s = offlineData;
                    flag = false;
                    break;
                } else c.moveToNext();
            }
        }
        _myDb.close();

        return s;
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
        Intent intent;
        switch(item.getItemId()) {
            case R.id.changeSchedule:
                intent = new Intent(MainSchedule.this, ChangeSchedule.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.updateSchedule:
                renderScheduleData(_query);
                break;
            case R.id.examsSchedule:

                break;
            case R.id.options:
                intent = new Intent(MainSchedule.this, Options.class);
                startActivity(intent);
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
    private DataBaseHelper _myDb;
    private SQLiteDatabase _db;
}
