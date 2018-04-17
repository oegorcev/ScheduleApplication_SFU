package Tester;

import android.os.AsyncTask;

import com.example.mrnobody43.shedule_application.MainSchedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import data.DataBase.DataBaseMapper;
import data.Parsers.AbstractParser;

/**
 * Created by Mr.Nobody43 on 17.04.2018.
 */

public class ParserTester {

    public ParserTester(MainSchedule mainSchedule) {
        _context = mainSchedule;
    }

    public void Test()
    {
        Reader reader = new Reader();
        reader.execute();
    }

    private void TestParser()
    {

        DataBaseMapper dataBaseMapper = new DataBaseMapper(_context);

        ArrayList<String> params = new ArrayList<String>();
        params = dataBaseMapper.getParseOptions();

        for(String s : _queryDb) {
            AbstractParser abstractParser = new AbstractParser(_context);
            abstractParser.execute(s, params.get(0), params.get(1));
            while (abstractParser.get_times() == null) {
            }
        }

        boolean ex = false;
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
            int resId = _context.getApplicationContext().getResources().getIdentifier(file, "raw", _context.getApplicationContext().getPackageName());
            InputStream inputStream = _context.getApplicationContext().getResources().openRawResource(resId);
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
            TestParser();
        }
    }


    private MainSchedule _context;
    private ArrayList<String> _queryDb;
}
