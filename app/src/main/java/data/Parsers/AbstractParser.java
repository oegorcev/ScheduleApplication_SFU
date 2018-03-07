package data.Parsers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Looper;
import android.widget.Toast;

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
import data.DataBase.DataBaseHelper;

/**
 * Created by Mr.Nobody43 on 15.01.2018.
 */

public class AbstractParser extends AsyncTask<String, Void, Void> implements IParser {

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) _mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public  AbstractParser(){}

    public AbstractParser(Context mContext)
    {
        this._mContext = mContext;
        _myDb = new DataBaseHelper(mContext);
    }

    @Override
    protected Void doInBackground(String... params) {
        Document doc = null;
        String s = "1";

        if(isNetworkAvailable()){
            doc = DownloadSchedule(params[0], params[1], params[2]);
            s = getCurrentWeek(params[1], params[2]);
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

        setCurrentWeek(s);

        return null;
    }

    public Document GetDbSchedule(String query){
        Document doc = null;

        _db = _myDb.getReadableDatabase();

        Cursor c = _db.query(DataBaseHelper.TABLE_NAME1, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            while (true) {
                if (c.isAfterLast()) break;

                int idIndex = c.getColumnIndex(DataBaseHelper.ID);
                int htmlIndex = c.getColumnIndex(DataBaseHelper.HTML_CODE);

                String offlineData = c.getString(htmlIndex);
                String bdId = c.getString(idIndex);
                if (query.equals(bdId)) {
                    doc = Jsoup.parse(offlineData);
                    break;
                } else c.moveToNext();
            }
        }
        _myDb.close();

        return doc;
    }

    public Document DownloadSchedule(String queury, String semestr, String potok) {
        Document doc = null;
        try {
            queury = queury.replace(" ", "%20");

            doc = Jsoup.connect(Constants.URL + queury + Constants.POTOK + potok + Constants.SEMESTR + semestr).get();

            _db = _myDb.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(DataBaseHelper.ID, queury);
            cv.put(DataBaseHelper.HTML_CODE, doc.toString());

            int was = _db.update(DataBaseHelper.TABLE_NAME1, cv, DataBaseHelper.ID + " = ?", new String[] { queury });
            if(was == 0)
            {
                _db.insert(DataBaseHelper.TABLE_NAME1, null, cv);
            }

            _myDb.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
    }

    public String getCurrentWeek(String semestr, String potok)
    {
        Document doc = null;
        String infoString = "";

        try {
            doc = Jsoup.connect(Constants.WEEK_URL + potok + Constants.SEMESTR + semestr).get();
            Element info = doc.getElementById(Constants.WEEK_INFO);
            infoString = info.text();
            String[] mas = infoString.split(" ");
            infoString = mas[1];

        } catch (IOException e) {
            Looper.prepare();
            _toast = Toast.makeText(_mContext, "Сервер недоступен!", Toast.LENGTH_SHORT);
            _toast.show();
            e.printStackTrace();
        }

        return infoString;
    }

    public void ParseDocument(Document doc) {
        Element tableWeek = doc.getElementById(Constants.WEEK_SCHEDULE);
        Element tableExams = doc.getElementById(Constants.EXAMS_SCHEDULE);

        _times = new ArrayList<String>();

        if(tableWeek != null){
            _scheduleMain = ParseTable(tableWeek);
        }
        if(tableExams != null) {
            _scheduleExams = ParseTable(tableExams);
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

    public void setTimes(ArrayList<String> _times) {
        this._times = _times;
    }

    public List<List<Pair<String, String>>> getScheduleMain() {
        return _scheduleMain;
    }

    public void setScheduleMain(List<List<Pair<String, String>>> _scheduleMain) {
        this._scheduleMain = _scheduleMain;
    }

    public List<List<Pair<String, String>>> getScheduleExams() {
        return _scheduleExams;
    }

    public void setScheduleExams(List<List<Pair<String, String>>> _scheduleExams) {
        this._scheduleExams = _scheduleExams;
    }

    public String getCurrentWeek() {
        return _currentWeek;
    }

    public void setCurrentWeek(String _currentWeek) {
        this._currentWeek = _currentWeek;
    }

    private ArrayList<String> _times;
    private List<List<Pair<String, String>>> _scheduleMain;
    private String _currentWeek;
    private List<List<Pair<String, String>>> _scheduleExams;
    private DataBaseHelper _myDb;
    private SQLiteDatabase _db;
    private Context _mContext;
    private Toast _toast;
}
