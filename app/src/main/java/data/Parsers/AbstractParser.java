package data.Parsers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import data.DataBase.DataBaseMapper;

/**
 * Created by Mr.Nobody43 on 15.01.2018.
 */

public class AbstractParser extends AsyncTask<String, Void, Void> implements IParser {

    public  AbstractParser(){}

    public AbstractParser(Context mContext)
    {
        this._mContext = mContext;
        _dataBaseMapper = new DataBaseMapper(mContext);
        _isServerBroken = false;
    }

    @Override
    protected Void doInBackground(String... params) {
        Document doc = null;

        if(isNetworkAvailable()){
            doc = DownloadSchedule(params[0], params[1], params[2]);
            downloadCurrentWeek(params[1], params[2]);
            if(doc == null){
                _isServerBroken = true;
                doc = _dataBaseMapper.getDbSchedule(params[0]);
            }
        }
        else {
            doc = _dataBaseMapper.getDbSchedule(params[0]);
        }

        if(doc != null) {
            ParseDocument(doc);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if(_isServerBroken) {
            Toast toast = Toast.makeText(_mContext, "Сервер недоступен!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public Document DownloadSchedule(String query, String semestr, String potok) {
        Document doc = null;
        try {
            query = query.replace(" ", "%20");
            doc = Jsoup.connect(Constants.URL + query + Constants.POTOK + potok + Constants.SEMESTR + semestr).timeout(2500).get();

            _dataBaseMapper.setNewSchedule(doc, query);

        } catch (IOException e) {

            e.printStackTrace();
        }

        return doc;
    }

    private void downloadCurrentWeek(String semestr, String potok)
    {
        Document doc = null;
        String infoString = "";

        try {
            doc = Jsoup.connect(Constants.WEEK_URL + potok + Constants.SEMESTR + semestr).timeout(2500).get();
            Element info = doc.getElementById(Constants.WEEK_INFO);
            infoString = info.text();
            String[] mas = infoString.split(" ");

            ArrayList<Integer> params = _dataBaseMapper.getSpinnerParams();

            /*Для семестра с чемпионатом мира*/
            if(params.get(1).equals(0) && params.get(0).equals(1)){
                Integer t = Integer.parseInt(mas[1]);
                t += 2;
                mas[1] = t.toString();
            }
            /*-------------------------------*/
            _dataBaseMapper.setWeek(mas[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) _mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public ArrayList<String> get_times() {
        return _times;
    }

    public List<List<Pair<String, String>>> getScheduleMain() {
        return _scheduleMain;
    }

    public List<List<Pair<String, String>>> getScheduleExams() {
        return _scheduleExams;
    }

    private ArrayList<String> _times;
    private List<List<Pair<String, String>>> _scheduleMain;
    private List<List<Pair<String, String>>> _scheduleExams;
    private Context _mContext;
    private boolean _isServerBroken;
    private DataBaseMapper _dataBaseMapper;
}
