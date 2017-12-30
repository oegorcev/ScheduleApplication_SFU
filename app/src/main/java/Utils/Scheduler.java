package Utils;

import java.util.ArrayList;
import java.util.List;

import model.Class;
import model.Day;
import model.Week;


public class Scheduler {

    public void make_schedule() {
        Parser parser = new Parser();

        parser.execute("КТбо4-8", "0", "1");

        ArrayList<String> times = parser.get_times();
        List<List<Pair<String,String>>> schedule = parser.get_schedule();

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

                _classTop = parser.parseClass(schedule.get(i).get(j).getFirst().split(" "));
                _classBot = parser.parseClass(schedule.get(i).get(j).getSecond().split(" "));

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

        return;
    }
}
