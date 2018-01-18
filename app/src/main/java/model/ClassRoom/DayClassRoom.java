package model.ClassRoom;

import java.util.ArrayList;

/**
 * Created by Mr.Nobody43 on 18.01.2018.
 */

public class DayClassRoom {

    private String _day_of_the_week;
    private ArrayList<ClassClassRoom> _classesTopWeek;
    private ArrayList<ClassClassRoom> _classesBotWeek;

    public String get_day_of_the_week() {
        return _day_of_the_week;
    }

    public void set_day_of_the_week(String _day_of_the_week) {
        this._day_of_the_week = _day_of_the_week;
    }

    public ArrayList<ClassClassRoom> get_classesTopWeek() {
        return _classesTopWeek;
    }

    public void set_classesTopWeek(ArrayList<ClassClassRoom> _classesTopWeek) {
        this._classesTopWeek = _classesTopWeek;
    }

    public ArrayList<ClassClassRoom> get_classesBotWeek() {
        return _classesBotWeek;
    }

    public void set_classesBotWeek(ArrayList<ClassClassRoom> _classesBotWeek) {
        this._classesBotWeek = _classesBotWeek;
    }
}
