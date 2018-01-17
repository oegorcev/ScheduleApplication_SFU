package model.Teacher;

import java.util.ArrayList;

/**
 * Created by Mr.Nobody43 on 17.01.2018.
 */

public class WeekTeacher {
    private ArrayList<DayTeacher> _week;

    public ArrayList<DayTeacher> getWeek() {
        return _week;
    }

    public void setWeek(ArrayList<DayTeacher> week) {
        this._week = week;
    }
}
