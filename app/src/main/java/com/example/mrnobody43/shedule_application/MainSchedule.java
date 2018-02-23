package com.example.mrnobody43.shedule_application;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import Utils.Constants;
import Utils.Utilities;
import adapters.MainScheduleFragmentAdapter;
import data.DataBaseHelper;
import data.Scheduler;
import model.ClassRoom.WeekClassRoom;
import model.Group.WeekGroup;
import model.Teacher.WeekTeacher;

public class MainSchedule extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_schedule);

        pager = (ViewPager) findViewById(R.id.pager);

        _myDb = new DataBaseHelper(this);

        _query = getCurruntQuery();

        renderScheduleData(_query);
    }

    public void renderScheduleData(String name) {

        _query = name;
        setTitle(_query);

        _currentScheduleClassRoom = null;
        _currentScheduleGroup = null;
        _currentScheduleTeacher = null;

        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.execute();
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

            Calendar newCal = new GregorianCalendar();
            int day = newCal.get(Calendar.DAY_OF_WEEK) - 2;
            if(day < 0){
                day = Constants.DAYS_ON_WEEK - 1;
            }

            if(pagerAdapter == null){
                pagerAdapter = new MainScheduleFragmentAdapter(getSupportFragmentManager(), MainSchedule.this, Utilities.SetState(_query));
                pager.setOffscreenPageLimit(6);
            } else
            {
                pagerAdapter.notifyDataSetChanged();

            }

            pagerAdapter.set_CURRENT_STATE(Utilities.SetState(_query));

            switch (Utilities.SetState(_query))
            {
                case Constants.GROUP: {
                    pagerAdapter.set_currentSchedule(_currentScheduleGroup);
                    break;
                }
                case Constants.TEACHER: {
                    pagerAdapter.set_currentSchedule(_currentScheduleTeacher);
                    break;
                }
                case Constants.CLASSROOM: {
                    pagerAdapter.set_currentSchedule(_currentScheduleClassRoom);
                    break;
                }
                default: {
                    break;
                }
            }

            pager.setAdapter(pagerAdapter);

            //pager.setCurrentItem(day);

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

    private String getCurruntQuery()
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.examsSchedule);
        item.setVisible(true);
        item = menu.findItem(R.id.mainSchedule);
        item.setVisible(false);

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
                intent = new Intent(MainSchedule.this, ExamsSchedule.class);
                startActivity(intent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            _query = data.getStringExtra(Constants.CHANGED_SCHEDULE);
            renderScheduleData(_query);
        }
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

    public void set_currentWeek(String _currentWeek) {
        this._currentWeek = _currentWeek;
    }

    ViewPager pager;
    MainScheduleFragmentAdapter pagerAdapter;
    private WeekGroup _currentScheduleGroup;
    private WeekTeacher _currentScheduleTeacher;
    private WeekClassRoom _currentScheduleClassRoom;
    private String _prev_query;
    private String _query = "";
    private String _currentWeek;
    private DataBaseHelper _myDb;
    private SQLiteDatabase _db;
}
