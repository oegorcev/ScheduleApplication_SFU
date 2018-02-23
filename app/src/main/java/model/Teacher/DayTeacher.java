package model.Teacher;

import java.util.ArrayList;

/**
 * Created by Mr.Nobody43 on 17.01.2018.
 */

public class DayTeacher {

    private String _day_of_the_week;
    private ArrayList<ClassTeacher> _classesTopWeek;
    private ArrayList<ClassTeacher> _classesBotWeek;

    public String get_day_of_the_week() {
        return _day_of_the_week;
    }

    public void set_day_of_the_week(String _day_of_the_week) {
        this._day_of_the_week = _day_of_the_week;
    }

    public ArrayList<ClassTeacher> get_classesTopWeek() {
        return _classesTopWeek;
    }

    public void set_classesTopWeek(ArrayList<ClassTeacher> _classesTopWeek) {
        this._classesTopWeek = _classesTopWeek;
    }

    public ArrayList<ClassTeacher> get_classesBotWeek() {
        return _classesBotWeek;
    }

    public void set_classesBotWeek(ArrayList<ClassTeacher> _classesBotWeek) {
        this._classesBotWeek = _classesBotWeek;
    }
}
