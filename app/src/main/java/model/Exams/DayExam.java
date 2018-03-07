package model.Exams;

import java.util.ArrayList;

/**
 * Created by Mr.Nobody43 on 07.03.2018.
 */

public class DayExam {

    private String _day_of_the_week;
    private ArrayList<ClassExam> _classes;

    public String get_day_of_the_week() {
        return _day_of_the_week;
    }

    public void set_day_of_the_week(String _day_of_the_week) {
        this._day_of_the_week = _day_of_the_week;
    }

    public ArrayList<ClassExam> get_classes() {
        return _classes;
    }

    public void set_classes(ArrayList<ClassExam> _classes) {
        this._classes = _classes;
    }
}
