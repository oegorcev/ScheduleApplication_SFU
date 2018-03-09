package com.example.mrnobody43.shedule_application;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import data.DataBase.DataBaseMapper;
import data.Scheduler;
import model.Exams.AllExams;

/**
 * Created by Mr.Nobody43 on 07.02.2018.
 */

public class ExamsSchedule extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams_schedule);
        _dataBaseMapper = new DataBaseMapper(this);


        _query = _dataBaseMapper.getCurruntQuery();
        renderScheduleData(_query);
    }

    public void renderScheduleData(String name) {

        _query = name;
        setTitle(_query  + " - Экзамены");

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
                renderScheduleData(_query);
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
        renderScheduleData(_query);
    }

    public void set_currentScheduleExams(AllExams _currentScheduleExams) {
        this._currentScheduleExams = _currentScheduleExams;
    }

    private AllExams _currentScheduleExams;
    private String _query = "";
    private DataBaseMapper _dataBaseMapper;
}

