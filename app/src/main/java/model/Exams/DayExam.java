package model.Exams;

import java.util.ArrayList;

/**
 * Created by Mr.Nobody43 on 07.03.2018.
 */

public class DayExam {

    private String _day;
    private ArrayList<ClassExam> _classes;

    public String get_day() {
        return _day;
    }

    public void set_day(String _day) {
        this._day = _day;
    }

    public ArrayList<ClassExam> get_classes() {
        return _classes;
    }

    public void set_classes(ArrayList<ClassExam> _classes) {
        this._classes = _classes;
    }
}
