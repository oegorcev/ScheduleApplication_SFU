package model.ExamGroup;

import java.util.ArrayList;

/**
 * Created by Mr.Nobody43 on 07.03.2018.
 */

public class DayExamGroup {

    private String _day;
    private ArrayList<ClassExamGroup> _classes;

    public String get_day() {
        return _day;
    }

    public void set_day(String _day) {
        this._day = _day;
    }

    public ArrayList<ClassExamGroup> get_classes() {
        return _classes;
    }

    public void set_classes(ArrayList<ClassExamGroup> _classes) {
        this._classes = _classes;
    }
}
