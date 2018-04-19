package com.example.mrnobody43.shedule_application;

import android.content.Intent;
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

import Tester.ParserTester;
import Utils.Constants;
import Utils.Utilities;
import adapters.MainScheduleFragmentAdapter;
import data.DataBase.DataBaseMapper;
import data.Scheduler;
import model.ClassRoom.WeekClassRoom;
import model.Group.WeekGroup;
import model.Teacher.WeekTeacher;

public class MainSchedule extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setTitle(Constants.EMPTY_STRING);
        setContentView(R.layout.activity_schedule);

        _dataBaseMapper = new DataBaseMapper(this);
        pager = (ViewPager) findViewById(R.id.pager);
        pb = findViewById(R.id.inflateProgressbar);

        _query = _dataBaseMapper.getCurruntQuery();
        _curWeek = Integer.parseInt(_dataBaseMapper.getCurrentWeek());


        boolean test = false;

        if(test) {
            ParserTester parserTester = new ParserTester(this);
            parserTester.Test();
        } else {
            renderScheduleData();
        }

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                Constants.lastPage = position;
            }
        });
    }

    public void renderScheduleData() {

        _currentScheduleClassRoom = null;
        _currentScheduleGroup = null;
        _currentScheduleTeacher = null;

        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.execute();
    }

    @Override
    public void onBackPressed() {
        String newQuery = Constants.getLastQuery();
        if(!newQuery.equals(Constants.EMPTY_STRING)) {
            _dataBaseMapper.setNewQuery(newQuery);
            _query = newQuery;
            renderScheduleData();
        }
        else{
            finish();
        }
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
                pagerAdapter = new MainScheduleFragmentAdapter(getSupportFragmentManager(), MainSchedule.this, Utilities.GetState(_query));
                //pager.setOffscreenPageLimit(6); //чекнуть два
            } else
            {
                pagerAdapter.notifyDataSetChanged();
            }

            pagerAdapter.set_showsWeek(_curWeek);
            pagerAdapter.set_CURRENT_STATE(Utilities.GetState(_query));

            switch (Utilities.GetState(_query))
            {
                case Constants.GROUP: {
                    if(_currentScheduleGroup != null) pagerAdapter.set_currentSchedule(_currentScheduleGroup);
                    else pagerAdapter.set_currentSchedule(new WeekGroup());
                    break;
                }
                case Constants.TEACHER: {
                    if(_currentScheduleTeacher != null)pagerAdapter.set_currentSchedule(_currentScheduleTeacher);
                    else pagerAdapter.set_currentSchedule(new WeekTeacher());
                    break;
                }
                case Constants.CLASSROOM: {
                    if(_currentScheduleClassRoom != null) pagerAdapter.set_currentSchedule(_currentScheduleClassRoom);
                    else pagerAdapter.set_currentSchedule(new WeekClassRoom());
                    break;
                }
                default: {
                    break;
                }
            }

            pager.setAdapter(pagerAdapter);
            if(Constants.lastPage == null)
            {
                pager.setCurrentItem(day);
                Constants.lastPage = day;
            }
            else pager.setCurrentItem(Constants.lastPage);

            setTitles();
            pb.setVisibility(View.GONE);
        }
    }

    private void setTitles() {
        String week = _dataBaseMapper.getCurrentWeek();
        setTitle(_query + Constants.SLASH_SEPARATOP + week + Constants.WEEK);
        if(getSupportActionBar()!= null) {
            if(_curWeek % 2 == 0){
                getSupportActionBar().setSubtitle(Constants.BOTTOM_WEEK_SCHEDULE);
            } else {
                getSupportActionBar().setSubtitle(Constants.TOP_WEEK_SCHEDULE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        _menu = menu;
        MenuItem item = menu.findItem(R.id.examsSchedule);
        item.setVisible(true);
        item = menu.findItem(R.id.mainSchedule);
        item.setVisible(false);

        _curWeek = Integer.parseInt(_dataBaseMapper.getCurrentWeek());

        if(_curWeek % 2 == 0) {
            item = menu.findItem(R.id.botWeek);
            item.setVisible(true);
            item = menu.findItem(R.id.upWeek);
            item.setVisible(false);
        } else {
            item = menu.findItem(R.id.botWeek);
            item.setVisible(true);
            item = menu.findItem(R.id.upWeek);
            item.setVisible(false);
        }

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
                renderScheduleData();
                break;
            case R.id.examsSchedule:
                intent = new Intent(MainSchedule.this, ExamsSchedule.class);
                startActivity(intent);
                break;
            case R.id.options:
                intent = new Intent(MainSchedule.this, Options.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.upWeek:
                item.setVisible(false);
                _curWeek++;
                _curWeek %= 2;
                item = _menu.findItem(R.id.botWeek);
                item.setVisible(true);
                renderScheduleData();
                break;
            case R.id.botWeek:
                _curWeek++;
                _curWeek %= 2;
                item.setVisible(false);
                item = _menu.findItem(R.id.upWeek);
                item.setVisible(true);
                renderScheduleData();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        _query = _dataBaseMapper.getCurruntQuery();
        renderScheduleData();
    }

    public void onTeacherClick(View V) {

        LinearLayout vwParentRow = (LinearLayout)V.getParent();

        TextView child = (TextView)vwParentRow.getChildAt(2);
        if(child == null) child = (TextView)vwParentRow.getChildAt(1);

        Constants.addQuery(_query);
        _query = child.getText().toString();
        _dataBaseMapper.setNewQuery(_query);

        renderScheduleData();
    }

    public void onClassroomClick(View V) {

        LinearLayout vwParentRow = (LinearLayout)V.getParent();

        Constants.addQuery(_query);
        TextView child = (TextView)vwParentRow.getChildAt(2);
        if(child == null) child = (TextView)vwParentRow.getChildAt(1);
        _query = child.getText().toString();
        _dataBaseMapper.setNewQuery(_query);

        renderScheduleData();
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

    View pb;
    ViewPager pager;
    MainScheduleFragmentAdapter pagerAdapter;
    private Menu _menu;
    private WeekGroup _currentScheduleGroup;
    private WeekTeacher _currentScheduleTeacher;
    private WeekClassRoom _currentScheduleClassRoom;
    private String _query = "";
    private Integer _curWeek;
    private DataBaseMapper _dataBaseMapper;
}
