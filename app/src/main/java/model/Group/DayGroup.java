package model.Group;

import java.util.ArrayList;

/**
 * Created by Mr.Nobody43 on 28.10.2017.
 */

public class DayGroup {

    private String _day_of_the_week;
    private ArrayList<ClassGroup> _classesTopWeek;
    private ArrayList<ClassGroup> _classesBotWeek;

    public String get_day_of_the_week() {
        return _day_of_the_week;
    }

    public void set_day_of_the_week(String _day_of_the_week) {
        this._day_of_the_week = _day_of_the_week;
    }

    public ArrayList<ClassGroup> get_classesTopWeek() {
        return _classesTopWeek;
    }

    public void set_classesTopWeek(ArrayList<ClassGroup> _classesTopWeek) {
        this._classesTopWeek = _classesTopWeek;
    }

    public ArrayList<ClassGroup> get_classesBotWeek() {
        return _classesBotWeek;
    }

    public void set_classesBotWeek(ArrayList<ClassGroup> _classesBotWeek) {
        this._classesBotWeek = _classesBotWeek;
    }
}

