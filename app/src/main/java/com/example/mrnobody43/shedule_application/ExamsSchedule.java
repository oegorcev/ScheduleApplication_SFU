package com.example.mrnobody43.shedule_application;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import adapters.ExamFragmentAdapter;
import data.DataBase.DataBaseMapper;
import data.Scheduler;
import model.Exams.AllExams;

/**
 * Created by Mr.Nobody43 on 07.02.2018.
 */

public class ExamsSchedule extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.colorRed)));
        setContentView(R.layout.activity_schedule);
        _dataBaseMapper = new DataBaseMapper(this);

        pager = (ViewPager) findViewById(R.id.pager);
        pb =  findViewById(R.id.inflateProgressbar);

        _query = _dataBaseMapper.getCurruntQuery();
        renderScheduleData();
    }

    public void renderScheduleData() {
        _currentScheduleExams = null;

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

            for(int iCnt = 0; iCnt <  _currentScheduleExams.getAll().size(); ++iCnt) {
                days.add(_currentScheduleExams.getAll().get(iCnt).get_day());
            }

            if(pagerAdapter == null){
                pagerAdapter = new ExamFragmentAdapter(getSupportFragmentManager(), ExamsSchedule.this, days);
                pager.setOffscreenPageLimit(1); //чекнуть два

            } else
            {
                pagerAdapter.notifyDataSetChanged();
            }

            if(_currentScheduleExams != null) pagerAdapter.set_allExams(_currentScheduleExams);
            else pagerAdapter.set_allExams(new AllExams());

            pager.setAdapter(pagerAdapter);

            setTitles();

            pb.setVisibility(View.GONE);
        }
    }

    private void setTitles() {
        setTitle(_query);
        if(getSupportActionBar()!= null) {
            getSupportActionBar().setSubtitle("Экзамены");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        _query = _dataBaseMapper.getCurruntQuery();
        renderScheduleData();
    }

    public void set_currentScheduleExams(AllExams _currentScheduleExams) {
        this._currentScheduleExams = _currentScheduleExams;
    }

    View pb;
    ViewPager pager;
    ExamFragmentAdapter pagerAdapter;
    private SwipeRefreshLayout _swipeContainer;
    private AllExams _currentScheduleExams;
    private String _query = "";
    private DataBaseMapper _dataBaseMapper;
}

