package Tester;

import android.content.Context;
import android.os.AsyncTask;

import com.example.mrnobody43.shedule_application.ExamsSchedule;
import com.example.mrnobody43.shedule_application.MainSchedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import data.DataBase.DataBaseMapper;
import data.Parsers.AbstractParser;
import data.Scheduler;

/**
 * Created by Mr.Nobody43 on 17.04.2018.
 */

public class ParserTester {

    public ParserTester(MainSchedule mainSchedule) {
        _main = mainSchedule;
    }
    public ParserTester(ExamsSchedule examsSchedule) {
        _exam = examsSchedule;
    }

    public void Test() {
        Reader reader = new Reader();
        reader.execute();
    }

    private void TestSchedulerMain() {

        for(String s : _queryDb) {
            MainSchedule temp = _main;
            Scheduler scheduler = new Scheduler(temp , s);
            scheduler.execute();

            int timer = 0;

            while (!scheduler.isEnd) {}

            while(timer < 1e5) {timer++;}

            System.out.println(s + " is Ok!");
        }

        System.out.println("All is done!");
    }

    private void TestSchedulerExam() {

        for(String s : _queryDb) {
            ExamsSchedule temp = _exam;
            Scheduler scheduler = new Scheduler(temp , s);
            scheduler.execute();

            int timer = 0;

            while (!scheduler.isEnd) {}

            while(timer < 1e5) {timer++;}

            System.out.println(s + " is Ok!");
        }

        System.out.println("All is done!");
    }


    private void TestParser() {

        DataBaseMapper dataBaseMapper = new DataBaseMapper(_main);

        ArrayList<String> params = new ArrayList<String>();
        params = dataBaseMapper.getParseOptions();

        for(String s : _queryDb) {
            AbstractParser abstractParser = new AbstractParser(_main);
            abstractParser.execute(s, params.get(0), params.get(1));
            while (abstractParser.get_times() == null) {
            }

            System.out.println(s + " is Ok!");
        }

        System.out.println("All is done!");
    }


    private class Reader extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            _queryDb = new ArrayList<String>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String file = "queries";
            Context context;
            if(_main != null) context = _main;
            else context = _exam;

            int resId = context.getApplicationContext().getResources().getIdentifier(file, "raw", context.getApplicationContext().getPackageName());
            InputStream inputStream = context.getApplicationContext().getResources().openRawResource(resId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 8192);
            try {
                String test;
                while (true) {
                    test = reader.readLine();
                    if (test == null)
                        break;
                    _queryDb.add(test);
                }
                inputStream.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(_main != null) TestSchedulerMain();
            else  TestSchedulerExam();
        }
    }

    private ExamsSchedule _exam;
    private MainSchedule _main;
    private ArrayList<String> _queryDb;
}
