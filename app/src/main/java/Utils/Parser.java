package Utils;


import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser extends AsyncTask<String, Void, Void> {

    String title;
    @Override
    protected Void doInBackground(String... params) {

        Document doc = null;
        try {
            doc = Jsoup.connect("http://asu.tti.sfedu.ru/Raspisanie/ShowRaspisanie.aspx?Substance=%D0%9A%D0%A2%D0%B1%D0%BE4-8&isPotok=121&Semestr=1").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element table = doc.getElementById("tblRaspis");

        Elements trs = table.getElementsByTag("tr");

        ArrayList<String> Times = new ArrayList<String>();

        List<List<Pair<String,String>>> schedule = new ArrayList<>();
        for (int cnt = 0; cnt < Constants.DAYS_ON_WEEK; ++cnt) schedule.add(new ArrayList<Pair<String,String>>());

        int cntI = 0;
        int curDay = 0;

        for (Element curTr: trs)
        {

            Elements tds = curTr.getElementsByTag("td");
            int cntJ = 0;

            for (Element curTd: tds)
            {

                if (cntI == Constants.DATE_INDEX)
                {
                    if(cntJ > Constants.BEGIN_TIME)
                    {
                        Elements strtTime = curTd.getElementsByClass(Constants.START_TIME);
                        Elements endTime = curTd.getElementsByClass(Constants.END_TIME);

                        String s = strtTime.get(0).html().concat(Constants.SEPARATOR).concat(endTime.get(0).html());

                        Times.add(s);
                    }
                }
                else if(cntI > 1)
                {
                    if(cntI % 2 == 0)
                    {
                        Pair<String,String> curPair = new Pair<String,String>(Constants.SEPARATOR, Constants.SEPARATOR);

                        if (curTd.attr(Constants.CLASS).equals(Constants.TOP_WEEK))
                        {
                            curPair.setFirst(curTd.html());
                            curPair.setSecond(Constants.RESERVED);
                        }
                        else
                        {
                            curPair.setFirst(curTd.html());
                            curPair.setSecond(curTd.html());
                        }

                        schedule.get(curDay).add(curPair);
                    }
                    else
                    {
                        int cntPair = 0;

                        for (Pair<String, String> curP: schedule.get(curDay))
                        {
                            if (curP.getSecond().equals(Constants.RESERVED))
                            {
                                schedule.get(curDay).get(cntPair).setSecond(curTd.html());
                            }

                            cntPair++;
                        }
                    }
                }

                cntJ++;
            }

            if(cntI > 1 && cntI % 2 == 0) curDay++;

            cntI++;
        }

        return null;
    }
}
