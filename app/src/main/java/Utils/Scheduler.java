package Utils;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import model.Class;
import model.Day;
import model.Week;

public class Scheduler extends AsyncTask<String, Void, Week> {

    private String _group;
    private Parser _parser;

    public Scheduler(String group) {
        super();

        _group = group;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        _parser = new Parser();
        _parser.execute(_group, "0", "1");
    }

    protected Week doInBackground(String... params) {

        ArrayList<String> times = _parser.get_times();
        List<List<Pair<String,String>>> schedule = _parser.get_schedule();

        Week week = new Week();
        ArrayList<Day> days = new ArrayList<Day>();

        for (int i = 0; i < 7; ++i) {
            Day curDay = new Day();

            curDay.set_day_of_the_week(schedule.get(i).get(0).getFirst());

            ArrayList<Class> classesTop = new ArrayList<Class>();
            ArrayList<Class> classesBot = new ArrayList<Class>();

            for (int j = 1; j < 8; ++j) {
                Class _classTop = new Class();
                Class _classBot = new Class();

                _classTop = _parser.parseClass(schedule.get(i).get(j).getFirst().split(" "));
                _classBot = _parser.parseClass(schedule.get(i).get(j).getSecond().split(" "));

                _classTop.set_time(times.get(j - 1));
                _classBot.set_time(times.get(j - 1));

                classesTop.add(_classTop);
                classesBot.add(_classBot);
            }

            curDay.set_classesTopWeek(classesTop);
            curDay.set_classesBotWeek(classesBot);

            days.add(curDay);
        }

        week.setWeek(days);

        return week;
    }
}
