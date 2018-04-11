package model.ExamTeacher;

import java.util.ArrayList;

/**
 * Created by Mr.Nobody43 on 11.04.2018.
 */

public class DayExamTeacher {
    private String _day;
    private ArrayList<ClassExamTeacher> _classes;

    public String get_day() {
        return _day;
    }

    public void set_day(String _day) {
        this._day = _day;
    }

    public ArrayList<ClassExamTeacher> get_classes() {
        return _classes;
    }

    public void set_classes(ArrayList<ClassExamTeacher> _classes) {
        this._classes = _classes;
    }
}
