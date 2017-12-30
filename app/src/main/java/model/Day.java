package model;

import java.util.ArrayList;

/**
 * Created by Mr.Nobody43 on 28.10.2017.
 */

public class Day {

    private String _day_of_the_week;
    private ArrayList<Class> _classesTopWeek;
    private ArrayList<Class> _classesBotWeek;

    public String get_day_of_the_week() {
        return _day_of_the_week;
    }

    public void set_day_of_the_week(String _day_of_the_week) {
        this._day_of_the_week = _day_of_the_week;
    }

    public ArrayList<Class> get_classesTopWeek() {
        return _classesTopWeek;
    }

    public void set_classesTopWeek(ArrayList<Class> _classesTopWeek) {
        this._classesTopWeek = _classesTopWeek;
    }

    public ArrayList<Class> get_classesBotWeek() {
        return _classesBotWeek;
    }

    public void set_classesBotWeek(ArrayList<Class> _classesBotWeek) {
        this._classesBotWeek = _classesBotWeek;
    }
}

