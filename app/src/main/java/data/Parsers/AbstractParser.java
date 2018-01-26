package data.Parsers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Utils.Constants;
import Utils.Pair;
import Utils.Utilities;
import data.DataBaseHelper;

/**
 * Created by Mr.Nobody43 on 15.01.2018.
 */

public class AbstractParser extends AsyncTask<String, Void, Void> implements IParser {

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public  AbstractParser(){}

    public AbstractParser(Context mContext)
    {
        this.mContext = mContext;
        myDb = new DataBaseHelper(mContext);
    }

    @Override
    protected Void doInBackground(String... params) {
        Document doc = null;

        if(isNetworkAvailable()){
            doc = DownloadSchedule(params[0], params[1], params[2]);
            if(doc == null){
                doc = GetDbSchedule(params[0]);
            }
        }
        else {
            doc = GetDbSchedule(params[0]);
        }

        if(doc != null) {
            ParseDocument(doc);
        }

        return null;
    }

    public Document GetDbSchedule(String query){
        Document doc = null;

        db = myDb.getReadableDatabase();

        Cursor c = db.query(DataBaseHelper.TABLE_NAME1, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            boolean flag = true;
            while (true) {
                if (c.isAfterLast()) break;

                int idIndex = c.getColumnIndex(DataBaseHelper.ID);
                int htmlIndex = c.getColumnIndex(DataBaseHelper.HTML_CODE);

                String offlineData = c.getString(htmlIndex);
                String bdId = c.getString(idIndex);
                if (query.equals(bdId)) {
                    doc = Jsoup.parse(offlineData);
                    flag = false;
                    break;
                } else c.moveToNext();
            }
        }
        myDb.close();

        return doc;
    }

    public Document DownloadSchedule(String queury, String semestr, String potok) {
        Document doc = null;
        try {
            queury = queury.replace(" ", "%20");
            //queury = queury.substring(0, queury.indexOf(' ')) + "%20" + queury.substring(queury.indexOf(' ') + 1);
            doc = Jsoup.connect(Constants.URL + queury + Constants.POTOK + potok + Constants.SEMESTR + semestr).get();

            db = myDb.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(DataBaseHelper.ID, queury);
            cv.put(DataBaseHelper.HTML_CODE, doc.toString());

            int was = db.update(DataBaseHelper.TABLE_NAME1, cv, DataBaseHelper.ID + " = ?", new String[] { queury });
            if(was == 0)
            {
                db.insert(DataBaseHelper.TABLE_NAME1, null, cv);
            }

            myDb.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
    }

    public void ParseDocument(Document doc) {
        Element tableWeek = doc.getElementById(Constants.WEEK_SCHEDULE);
        Element tableExams = doc.getElementById(Constants.EXAMS_SCHEDULE);

        _times = new ArrayList<String>();

        if(tableWeek != null){
            _schedule_main = ParseTable(tableWeek);
        }
        if(tableExams != null) {
            _schedule_exams = ParseTable(tableExams);
        }
    }

    private List<List<Pair<String, String>>> ParseTable(Element table)
    {
        Elements trs = table.getElementsByTag(Constants.PARSE_TAG_DAYS);

        List<List<Pair<String, String>>> schedule = new ArrayList<>();

        /*
            Цикл идёт по количеству тегов tr, делим на два, потому что
            две недели и минус один из-за шапки.
         */
        for (int cnt = Constants.DEFAULT_VALUE_CNT_PARSER; cnt < trs.size()  / 2 - 1; ++cnt)
            schedule.add(new ArrayList<Pair<String, String>>());

        int cntI = Constants.DEFAULT_VALUE_CNT_PARSER;
        int curDay = Constants.DEFAULT_VALUE_CNT_PARSER;

        for (Element curTr : trs) {
            Elements tds = curTr.getElementsByTag(Constants.PARSE_TAG_ELEMENTS);
            int cntJ = Constants.DEFAULT_VALUE_CNT_PARSER;

            for (Element curTd : tds) {
                if (cntI == Constants.DATE_INDEX) {
                    if (cntJ >= Constants.BEGIN_TIME && _times.size() < Constants.DAYS_ON_WEEK) {
                        Elements strtTime = curTd.getElementsByClass(Constants.START_TIME);
                        Elements endTime = curTd.getElementsByClass(Constants.END_TIME);

                        String s = strtTime.get(0).html().concat(Constants.SEPARATOR).concat(endTime.get(0).html());

                        _times.add(s);
                    }
                } else if (cntI > Constants.DATE_INDEX) {
                    if (Utilities.isEven(cntI)) {
                        Pair<String, String> curPair = new Pair<String, String>(Constants.SEPARATOR, Constants.SEPARATOR);

                        if (curTd.attr(Constants.CLASS).equals(Constants.TOP_WEEK)) {
                            curPair.setFirst(curTd.html());
                            curPair.setSecond(Constants.RESERVED);
                        } else {
                            curPair.setFirst(curTd.html());
                            curPair.setSecond(curTd.html());
                        }

                        schedule.get(curDay).add(curPair);
                    } else {
                        int cntPair = Constants.DEFAULT_VALUE_CNT_PARSER;

                        for (Pair<String, String> curP : schedule.get(curDay)) {
                            if (curP.getSecond().equals(Constants.RESERVED)) {
                                schedule.get(curDay).get(cntPair).setSecond(curTd.html());
                                break;
                            }

                            cntPair++;
                        }
                    }
                }

                cntJ++;
            }

            if (cntI > Constants.DATE_INDEX && !Utilities.isEven(cntI)) curDay++;

            cntI++;
        }

        return schedule;
    }

    public ArrayList<String> get_times() {
        return _times;
    }

    public void set_times(ArrayList<String> _times) {
        this._times = _times;
    }

    public List<List<Pair<String, String>>> get_schedule_main() {
        return _schedule_main;
    }

    public void set_schedule_main(List<List<Pair<String, String>>> _schedule_main) {
        this._schedule_main = _schedule_main;
    }

    public List<List<Pair<String, String>>> get_schedule_exams() {
        return _schedule_exams;
    }

    public void set_schedule_exams(List<List<Pair<String, String>>> _schedule_exams) {
        this._schedule_exams = _schedule_exams;
    }


    private ArrayList<String> _times;
    private List<List<Pair<String, String>>> _schedule_main;
    private List<List<Pair<String, String>>> _schedule_exams;
    private DataBaseHelper myDb;
    private SQLiteDatabase db;
    private Context mContext;
}
