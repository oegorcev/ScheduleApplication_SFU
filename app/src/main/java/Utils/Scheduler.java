package Utils;

import android.os.AsyncTask;

import com.example.mrnobody43.shedule_application.StartScreen;

import java.util.ArrayList;
import java.util.List;

import model.Class;
import model.Day;
import model.Week;

public class Scheduler extends AsyncTask<StartScreen, Void, Void> {

    private String _query;
    private Parser _parser;
    private StartScreen _startScreen;

    public Scheduler(StartScreen startScreen, String query) {
        super();
        _query = query;
        _startScreen = startScreen;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        _parser = new Parser();
        _parser.execute(_query, "1");
    }

    protected Void doInBackground(StartScreen... params) {

        ArrayList<String> times = _parser.get_times();
        List<List<Pair<String,String>>> schedule = _parser.get_schedule();

        Week week = new Week();
        ArrayList<Day> days = new ArrayList<Day>();

        //7 -
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
        _startScreen.set_currentSchedule(week);

        return null;
    }
}
