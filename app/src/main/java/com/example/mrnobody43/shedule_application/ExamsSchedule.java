package com.example.mrnobody43.shedule_application;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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

import java.util.ArrayList;

import Tester.ParserTester;
import Utils.Constants;
import Utils.Utilities;
import adapters.ExamFragmentAdapter;
import data.DataBase.DataBaseMapper;
import data.Scheduler;
import model.ExamClassroom.ExamsClassroom;
import model.ExamGroup.ExamsGroup;
import model.ExamTeacher.ExamsTeacher;

/**
 * Created by Mr.Nobody43 on 07.02.2018.
 */

public class ExamsSchedule extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(Constants.EMPTY_STRING);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.colorRed)));
        setContentView(R.layout.activity_schedule);
        _dataBaseMapper = new DataBaseMapper(this);

        pager = (ViewPager) findViewById(R.id.pager);
        pb =  findViewById(R.id.inflateProgressbar);

        _query = _dataBaseMapper.getCurruntQuery();


        boolean test = false;

        if(test) {
            ParserTester parserTester = new ParserTester(this);
            parserTester.Test();
        } else {
            renderScheduleData();
        }
    }

    public void renderScheduleData() {
        _currentScheduleExamsGroup = null;

        ExamsSchedule.ScheduleTask scheduleTask = new ExamsSchedule.ScheduleTask();
        scheduleTask.execute();
    }

    private class  ScheduleTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            Scheduler scheduler = new Scheduler(ExamsSchedule.this,_query);
            scheduler.execute();
        }

        @Override
        protected Void doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayList<String> days = new ArrayList<String>();

            switch (Utilities.GetState(_query))
            {
                case Constants.GROUP: {
                    for (int iCnt = 0; iCnt < _currentScheduleExamsGroup.getAll().size(); ++iCnt) {
                        days.add(_currentScheduleExamsGroup.getAll().get(iCnt).get_day());
                    }
                    if(_currentScheduleExamsGroup.getAll().size() == 0){
                        days.add(Constants.EMPTY_EXAMS);
                    }
                    break;
                }
                case Constants.TEACHER: {
                    for (int iCnt = 0; iCnt < _currentScheduleExamsTeacher.getAll().size(); ++iCnt) {
                        days.add(_currentScheduleExamsTeacher.getAll().get(iCnt).get_day());
                    }
                    if(_currentScheduleExamsTeacher.getAll().size() == 0){
                        days.add(Constants.EMPTY_EXAMS);
                    }
                    break;
                }
                case Constants.CLASSROOM: {
                    for (int iCnt = 0; iCnt < _currentScheduleExamsClassroom.getAll().size(); ++iCnt) {
                        days.add(_currentScheduleExamsClassroom.getAll().get(iCnt).get_day());
                    }
                    if(_currentScheduleExamsClassroom.getAll().size() == 0){
                        days.add(Constants.EMPTY_EXAMS);
                    }
                    break;
                }
                default: {
                    break;
                }
            }

            if(pagerAdapter == null){
                pagerAdapter = new ExamFragmentAdapter(getSupportFragmentManager(), ExamsSchedule.this, days);
                pager.setOffscreenPageLimit(1); //чекнуть два

            } else
            {
                pagerAdapter.notifyDataSetChanged();
            }

            pagerAdapter.set_CURRENT_STATE(Utilities.GetState(_query));

            switch (Utilities.GetState(_query))
            {
                case Constants.GROUP: {
                    if(_currentScheduleExamsGroup != null) pagerAdapter.set_currentSchedule(_currentScheduleExamsGroup);
                    else pagerAdapter.set_currentSchedule(new ExamsGroup());
                    break;
                }
                case Constants.TEACHER: {
                    if(_currentScheduleExamsTeacher != null)pagerAdapter.set_currentSchedule(_currentScheduleExamsTeacher);
                    else pagerAdapter.set_currentSchedule(new ExamsGroup());
                    break;
                }
                case Constants.CLASSROOM: {
                    if(_currentScheduleExamsClassroom != null) pagerAdapter.set_currentSchedule(_currentScheduleExamsClassroom);
                    else pagerAdapter.set_currentSchedule(new ExamsClassroom());
                    break;
                }
                default: {
                    break;
                }
            }

            pager.setAdapter(pagerAdapter);

            setTitles();

            pb.setVisibility(View.GONE);
        }
    }

    private void setTitles() {
        setTitle(_query);
        if(getSupportActionBar()!= null) {
            getSupportActionBar().setSubtitle(Constants.EXAMS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.examsSchedule);
        item.setVisible(false);
        item = menu.findItem(R.id.mainSchedule);
        item.setVisible(true);
        item = menu.findItem(R.id.botWeek);
        item.setVisible(false);
        item = menu.findItem(R.id.upWeek);
        item.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.changeSchedule:
                intent = new Intent(ExamsSchedule.this, ChangeSchedule.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.updateSchedule:
                renderScheduleData();
                break;
            case R.id.mainSchedule:
                intent = new Intent(ExamsSchedule.this, MainSchedule.class);
                startActivity(intent);
                break;
            case R.id.options:
                intent = new Intent(ExamsSchedule.this, Options.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    public void onTeacherClick(View V) {

        LinearLayout vwParentRow = (LinearLayout)V.getParent();

        TextView child = (TextView)vwParentRow.getChildAt(1);

        Constants.addQuery(_query);
        _query = child.getText().toString();
        _dataBaseMapper.setNewQuery(_query);

        renderScheduleData();
    }

    public void onClassroomClick(View V) {

        LinearLayout vwParentRow = (LinearLayout)V.getParent();

        Constants.addQuery(_query);
        TextView child = (TextView)vwParentRow.getChildAt(2);
        _query = child.getText().toString();
        _dataBaseMapper.setNewQuery(_query);

        renderScheduleData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        _query = _dataBaseMapper.getCurruntQuery();
        renderScheduleData();
    }

    public void set_currentScheduleExamsGroup(ExamsGroup _currentScheduleExamsGroup) {
        this._currentScheduleExamsGroup = _currentScheduleExamsGroup;
    }

    public void set_currentScheduleExamsTeacher(ExamsTeacher _currentScheduleExamsTeacher) {this._currentScheduleExamsTeacher = _currentScheduleExamsTeacher;}

    public void set_currentScheduleExamsClassroom(ExamsClassroom _currentScheduleExamsClassroom) {this._currentScheduleExamsClassroom = _currentScheduleExamsClassroom;}

    View pb;
    ViewPager pager;
    ExamFragmentAdapter pagerAdapter;
    private ExamsGroup _currentScheduleExamsGroup;
    private ExamsTeacher _currentScheduleExamsTeacher;
    private ExamsClassroom _currentScheduleExamsClassroom;
    private String _query = "";
    private DataBaseMapper _dataBaseMapper;
}

